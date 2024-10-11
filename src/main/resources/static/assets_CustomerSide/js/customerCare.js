var socket = new SockJS('/ws'); // Kết nối đến WebSocket với SockJS
var stompClient = Stomp.over(socket);
var currentSessionId = null;

// Kết nối tới WebSocket server
stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);

    // Lắng nghe yêu cầu kết nối từ client
    stompClient.subscribe('/topic/staff/' + staffId, function (message) {
        const request = JSON.parse(message.body);
        handleConnectionRequest(request);
    });
});

// Xử lý yêu cầu kết nối từ client
function handleConnectionRequest(request) {
    var confirmModal = document.getElementById("confirmModal");
    var confirmMessage = document.getElementById("confirmMessage");
    var confirmApprove = document.getElementById("confirmApprove");
    var confirmReject = document.getElementById("confirmReject");

    // Thiết lập nội dung cho modal
    confirmMessage.innerHTML = `Client muốn kết nối với bạn. Bạn có chấp nhận không?`;

    // Hiển thị modal
    confirmModal.style.display = "block";

    const clientId = request.clientId;
    currentSessionId = request.content;

    // Xử lý khi staff nhấn "Chấp nhận"
    confirmApprove.onclick = function () {
        sendConnectionResponse("Accepted", staffId, clientId, currentSessionId);
        listenForMessages(currentSessionId);  // Bắt đầu lắng nghe tin nhắn từ client
        confirmModal.style.display = "none";
    };

    // Xử lý khi staff nhấn "Từ chối"
    confirmReject.onclick = function () {
        sendConnectionResponse("Rejected", staffId, clientId, currentSessionId);
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
function sendConnectionResponse(response, staffId, clientId, sessionId) {
    stompClient.send("/app/staff/confirmConnection", {}, JSON.stringify({
        staffId: staffId,
        clientId: clientId,
        status: response,
        content: sessionId
    }));
}

// Lắng nghe tin nhắn từ client trong phiên chat
function listenForMessages(sessionId) {
    stompClient.subscribe('/topic/chat/' + sessionId, function (message) {
        var chatMessage = JSON.parse(message.body);
        if (chatMessage.sender === "Staff") {
            displaySent(chatMessage.content);
        } else {
            displayReceived(chatMessage.content);
        }
    });
}

// Hiển thị tin nhắn lên giao diện
function displaySent(content) {
    var messageElement = document.createElement('p');
    messageElement.textContent = content;
    messageElement.setAttribute("class", "rep");
    document.getElementById('chatBox').appendChild(messageElement);
    handleScroll();
}

// Hiển thị tin nhắn lên giao diện
function displayReceived(content) {
    var messageElement = document.createElement('p');
    messageElement.textContent = content;
    messageElement.setAttribute("class", "send");
    document.getElementById('chatBox').appendChild(messageElement);
    handleScroll();
}

// Sự kiện gửi tin nhắn khi staff nhấn nút gửi
function sendButton() {
    var messageContent = document.getElementById('messageField').value;
    if (messageContent.trim() !== '') {
        sendMessage(messageContent);
    }
}

// Hàm xử lý cuộn xuống khi có tin nhắn mới
function handleScroll() {
    var elem = document.getElementById('chat-box');
    elem.scrollTop = elem.scrollHeight;
}

// Gửi tin nhắn từ staff
function sendMessage(content) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
            sender: "Staff",
            content: content,
            sessionId: currentSessionId
        }));
        document.getElementById('messageField').value = ''; // Clear input field after sending
    }
}

