// Khởi tạo WebSocket connection và stompClient
var socket;
var stompClient;
var currentSessionId = null;

// Lắng nghe phản hồi từ staff
function handleStaffResponse(clientId) {
    // Đăng ký lắng nghe phản hồi từ server cho client hiện tại
    socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // Lắng nghe yêu cầu kết nối từ staff
        stompClient.subscribe('/topic/client/' + clientId, function (message) {
            var data = JSON.parse(message.body);
            if (data.status === 'Accepted') {
                currentSessionId = data.content;
                setupWebSocket(currentSessionId); // Khởi tạo WebSocket nếu staff chấp nhận
            } else if (data.status === 'Rejected') {
                displayMessage("Staff này đang bận, vui lòng chọn Staff khác.");
                disconnectWebSocket(); // Ngắt WebSocket nếu staff từ chối
            }
        });
    });
}

// Hàm khởi tạo WebSocket khi staff chấp nhận kết nối
function setupWebSocket(sessionId) {
    // Đăng ký lắng nghe tin nhắn chat từ staff
    stompClient.subscribe('/topic/chat/' + sessionId, function (message) {
        var chatMessage = JSON.parse(message.body);
        if (chatMessage.sender === "Client") {
            displaySentMessage(chatMessage.content);
        } else {
            displayReceivedMessage(chatMessage.content);
        }
    });

    clearChatBox(); // Xóa tất cả tin nhắn cũ trong chat box
    directMode(); // Bật chế độ direct mode sau khi kết nối thành công
}

// Hàm ngắt kết nối WebSocket khi staff từ chối hoặc kết thúc chat
function disconnectWebSocket() {
    if (stompClient !== null) {
        stompClient.disconnect();
        console.log("WebSocket connection closed");
        botMode();
    }
}

// Gửi tin nhắn qua WebSocket
function sendMessage(content) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
            sender: "Client",
            content: content,
            sessionId: currentSessionId // Gửi tin nhắn dựa trên sessionId
        }));
        document.getElementById('messageField').value = ''; // Clear input field after sending
    }
}

// Sự kiện nhấn nút gửi tin nhắn
function sendButtonPressed() {
    var messageContent = document.getElementById('messageField').value;
    if (messageContent.trim() !== '') {
        sendMessage(messageContent);
    }
}

// Hiển thị tin nhắn lên giao diện
function displayMessage(content) {
    var messageElement = document.createElement('p');
    messageElement.textContent = content;
    messageElement.setAttribute("class", "msg");
    document.getElementById('chat-box').appendChild(messageElement);
    handleScroll();
}

// Hiển thị tin nhắn lên giao diện
function displaySentMessage(content) {
    var messageElement = document.createElement('p');
    messageElement.textContent = content;
    messageElement.setAttribute("class", "rep");
    document.getElementById('chat-box').appendChild(messageElement);
    handleScroll();
}

// Hiển thị tin nhắn lên giao diện
function displayReceivedMessage(content) {
    var messageElement = document.createElement('p');
    messageElement.textContent = content;
    messageElement.setAttribute("class", "send");
    document.getElementById('chat-box').appendChild(messageElement);
    handleScroll();
}

// Hàm xử lý cuộn xuống khi có tin nhắn mới
function handleScroll() {
    var elem = document.getElementById('chat-box');
    elem.scrollTop = elem.scrollHeight;
}

function directMode() {
    document.getElementById('messageField').readOnly = false;
    document.getElementById('resetChat').style.display = 'none';
    document.getElementById('endChat').style.display = 'inline-block';
}

function botMode() {
    document.getElementById('messageField').readOnly = true;
    document.getElementById('resetChat').style.display = 'inline-block';
    document.getElementById('endChat').style.display = 'none';
}

function clearChatBox() {
    var chatBox = document.getElementById('chat-box');
    while (chatBox.firstChild) {
        chatBox.removeChild(chatBox.firstChild);
    }
}
