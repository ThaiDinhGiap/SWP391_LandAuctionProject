document.addEventListener('DOMContentLoaded', function () {
    var socket = new SockJS('/ws'); // Kết nối đến WebSocket với SockJS
    var stompClient = Stomp.over(socket);
    var currentSessionId = null; // ID của phiên chat hiện tại

// Kết nối tới WebSocket server
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // Lắng nghe yêu cầu kết nối từ client
        stompClient.subscribe('/topic/staff/' + staffId, function (message) {
            const request = JSON.parse(message.body);
            const clientId = request.clientId// Parse nội dung yêu cầu kết nối từ client
            handleConnectionRequest(request, clientId);
        });
    });

// Xử lý yêu cầu kết nối từ client
    function handleConnectionRequest(request, clientId) {
        var confirmModal = document.getElementById("confirmModal");
        var confirmMessage = document.getElementById("confirmMessage");
        var confirmApprove = document.getElementById("confirmApprove");
        var confirmReject = document.getElementById("confirmReject");

        // Thiết lập nội dung cho modal
        confirmMessage.innerHTML = `Client muốn kết nối với bạn. Bạn có chấp nhận không?`;

        // Hiển thị modal
        confirmModal.style.display = "block";

        // Xử lý khi staff nhấn "Chấp nhận"
        confirmApprove.onclick = function () {
            currentSessionId = request.sessionId; // Lưu sessionId của phiên chat
            sendConnectionResponse("Accepted", staffId, clientId);
            listenForMessages(currentSessionId);  // Bắt đầu lắng nghe tin nhắn từ client
            confirmModal.style.display = "none";
        };

        // Xử lý khi staff nhấn "Từ chối"
        confirmReject.onclick = function () {
            sendConnectionResponse("Rejected", staffId, clientId);
            confirmModal.style.display = "none";
        };

        // Đóng modal khi nhấn dấu "X"
        document.getElementById("modalClose").onclick = function () {
            confirmModal.style.display = "none";
        };

        // Đóng modal nếu nhấn ngoài modal
        window.onclick = function (event) {
            if (event.target == confirmModal) {
                confirmModal.style.display = "none";
            }
        };
    }


// Gửi phản hồi kết nối đến server
    function sendConnectionResponse(accepted, staffId, clientId) {
        stompClient.send("/app/staff/confirmConnection", {}, JSON.stringify({
            staffId: staffId,
            clientId: clientId,
            status: accepted,
            content: `Client muốn kết nối với bạn. Bạn có muốn chấp nhận không?`
        }));
    }

// Lắng nghe tin nhắn từ client trong phiên chat
    function listenForMessages(sessionId) {
        stompClient.subscribe('/topic/chat/' + sessionId, function (message) {
            var chatMessage = JSON.parse(message.body);
            displayMessage(chatMessage.content);
        });
    }

// Hiển thị tin nhắn lên giao diện
    function displayMessage(content) {
        var messageElement = document.createElement('p');
        messageElement.textContent = content;
        document.getElementById('chatBox').appendChild(messageElement);
    }

// Sự kiện gửi tin nhắn khi staff nhấn nút gửi
    document.getElementById('sendButton').addEventListener('click', function () {
        var messageContent = document.getElementById('messageInput').value;
        sendMessage(messageContent);
    });

// Gửi tin nhắn từ staff
    function sendMessage(content) {
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
            sender: "Staff",
            content: content,
            sessionId: currentSessionId
        }));
    }
})
