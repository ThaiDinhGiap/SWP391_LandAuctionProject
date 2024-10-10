// Khởi tạo WebSocket connection và stompClient
var socket;
var stompClient;

// Hàm khởi tạo WebSocket khi staff chấp nhận kết nối
function setupWebSocket(staff) {
    console.log('Setting up WebSocket with: ' + staff.fullName);

    // Khởi tạo WebSocket và kết nối đến server
    socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    // Kết nối tới WebSocket server
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // Đăng ký lắng nghe tin nhắn chat từ staff
        stompClient.subscribe('/topic/chat/' + staff.staffId, function (message) {
            var chatMessage = JSON.parse(message.body);
            displayMessage(chatMessage.content);
        });
    });
}

// Hàm ngắt kết nối WebSocket khi staff từ chối hoặc kết thúc chat
function disconnectWebSocket() {
    if (stompClient !== null) {
        stompClient.disconnect();
        console.log("WebSocket connection closed");
    }
}

// Lắng nghe phản hồi từ staff
function handleStaffResponse(clientId, staff) {
    // Đăng ký lắng nghe phản hồi từ server cho client hiện tại
    socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // Lắng nghe yêu cầu kết nối từ staff
        stompClient.subscribe('/topic/client/' + clientId, function (message) {
            var data = JSON.parse(message.body);
            if (data.status === 'Accepted') {
                setupWebSocket(staff); // Khởi tạo WebSocket nếu staff chấp nhận
            } else if (data.status === 'Rejected') {
                displayMessage("Staff này đang bận, vui lòng chọn Staff khác.");
                disconnectWebSocket(); // Ngắt WebSocket nếu staff từ chối
            }
        });
    });
}

// Gửi tin nhắn qua WebSocket
function sendMessage(content) {
    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
        sender: "Client",
        content: content
    }));
}

// Hiển thị tin nhắn lên giao diện
function displayMessage(content) {
    var messageElement = document.createElement('p');
    messageElement.textContent = content;
    messageElement.setAttribute("class", "msg");
    cbot.appendChild(messageElement);
    handleScroll();
}

// Hàm xử lý cuộn xuống khi có tin nhắn mới
function handleScroll() {
    var elem = document.getElementById('chat-box');
    elem.scrollTop = elem.scrollHeight;
}
