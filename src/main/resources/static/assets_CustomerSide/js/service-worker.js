self.addEventListener('push', function(event) {
    const data = event.data.json();
    const title = data.title || 'Thông báo mới';
    const options = {
        body: data.message,
        icon: '/images/ins.png'
    };

    event.waitUntil(self.registration.showNotification(title, options));
});
