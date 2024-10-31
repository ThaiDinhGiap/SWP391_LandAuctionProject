if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('/assets_CustomerSide/js/service-worker.js')
        .then(async registration => {
            console.log('Service Worker registered with scope:', registration.scope);

            // Kiểm tra xem đã có Subscription tồn tại chưa
            const existingSubscription = await registration.pushManager.getSubscription();
            if (existingSubscription) {
                console.log("Existing Subscription found:", existingSubscription);
                return; // Dừng lại nếu đã có Subscription
            }

            // Gọi API để lấy VAPID Public Key
            const vapidPublicKey = await fetch('/api/config')
                .then(response => response.json())
                .then(config => config.vapidPublicKey)
                .catch(error => {
                    console.error('Error fetching config:', error);
                    return null;
                });

            if (vapidPublicKey) {
                console.log("Received VAPID Public Key:", vapidPublicKey);

                // Tạo Uint8Array từ tọa độ X và Y của VAPID Public Key
                function hexToUint8Array(hexString) {
                    let bytes = [];
                    for (let i = 0; i < hexString.length; i += 2) {
                        bytes.push(parseInt(hexString.substring(i, i + 2), 16));
                    }
                    return new Uint8Array(bytes);
                }

                const x = "71eeb81ecc049ce72fd715c682e8e96fdf1f20b2e9ffd19264164d22e8b42ba1";
                const y = "130d6b4d6e5850ac9ddcd7f39aa4639245da4edd8622dc6035b2262f899bb161";

                // Ghép "04" với X và Y để tạo khóa không nén
                const publicKeyHex = "04" + x + y;
                const applicationServerKey = hexToUint8Array(publicKeyHex);

                console.log("Converted applicationServerKey:", applicationServerKey);

                // Đăng ký push notification với VAPID Key nhận từ API
                const subscription = await registration.pushManager.subscribe({
                    userVisibleOnly: true,
                    applicationServerKey: applicationServerKey
                });

                // Tạo `SubscriptionRequest` từ `subscription`
                const subscriptionRequest = {
                    endpoint: subscription.endpoint,
                    p256dh: subscription.getKey('p256dh') ? btoa(String.fromCharCode.apply(null, new Uint8Array(subscription.getKey('p256dh')))) : null,
                    auth: subscription.getKey('auth') ? btoa(String.fromCharCode.apply(null, new Uint8Array(subscription.getKey('auth')))) : null
                };

                // Gửi subscription lên server cùng với `accountId`
                await fetch('/api/notifications/subscribe?accountId=' + clientId, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(subscriptionRequest)
                });
                console.log("Subscription sent to server");
            } else {
                console.error("Failed to retrieve VAPID Public Key.");
            }
        })
        .catch(error => {
            console.error('Service Worker registration failed:', error);
        });
}

// Yêu cầu quyền gửi thông báo
Notification.requestPermission().then(permission => {
    if (permission === "granted") {
        console.log("Permission for notifications granted.");
    } else {
        console.log("Permission for notifications denied.");
    }
});
