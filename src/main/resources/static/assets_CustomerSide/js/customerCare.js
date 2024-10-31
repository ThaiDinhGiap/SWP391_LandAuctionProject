var socket = new SockJS('/ws'); // Kết nối đến WebSocket với SockJS
var stompClient = Stomp.over(socket);
var currentSessionId = null;
var subscriptions = new Map();

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
    const sessionId = request.content;

    // Xử lý khi staff nhấn "Chấp nhận"
    confirmApprove.onclick = function () {
        sendConnectionResponse("Accepted", staffId, clientId, sessionId);
        listenForMessages(sessionId);
        addClientToLocalStorage(clientId, sessionId);// Bắt đầu lắng nghe tin nhắn từ client
        addClientToList(clientId, sessionId); // Thêm client vào danh sách
        confirmModal.style.display = "none";
    };

    // Xử lý khi staff nhấn "Từ chối"
    confirmReject.onclick = function () {
        sendConnectionResponse("Rejected", staffId, clientId, sessionId);
        confirmModal.style.display = "none";
    };

    // Đóng modal nếu nhấn ngoài modal
    window.onclick = function (event) {
        if (event.target === confirmModal) {
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
    const subscription = stompClient.subscribe('/topic/chat/' + sessionId, function (message) {
        var chatMessage = JSON.parse(message.body);
        if (chatMessage.sender === "Server") {
            unsubscribeFromTopic(chatMessage.sessionId);
        } else {
            if (chatMessage.sessionId === currentSessionId) {
                if (chatMessage.sender === "Staff") {
                    displaySent(chatMessage.content);
                } else if (chatMessage.sender === "Client") {
                    displayReceived(chatMessage.content);
                }
            }
        }
    });
    // Thêm subscription vào Map để quản lý
    subscriptions.set(sessionId, subscription);
}

function addClientToLocalStorage(clientId, sessionId) {
    // Lấy danh sách các client hiện có trong localStorage, nếu chưa có thì trả về mảng rỗng
    const clients = JSON.parse(localStorage.getItem('clientList')) || [];

    // Thêm client mới vào mảng client
    clients.push({ clientId: clientId, sessionId: sessionId });

    // Cập nhật lại localStorage với danh sách client mới
    localStorage.setItem('clientList', JSON.stringify(clients));
}


function addClientToList(clientId, sessionId) {
    const clientList = document.querySelector('.client-list');
    const clientItem = document.createElement('div');
    clientItem.className = 'client-item';
    clientItem.dataset.sessionId = sessionId; // Lưu sessionId vào thuộc tính của phần tử
    clientItem.innerHTML = `
        <span class="client-name">${clientId}</span>
    `;
    clientItem.onclick = function () {
        // Xóa lớp `selected-client` khỏi tất cả các `client-item` khác
        document.querySelectorAll('.client-item').forEach(item => {
            item.classList.remove('selected-client');
        });

        // Thêm lớp `selected-client` vào `clientItem` được chọn
        clientItem.classList.add('selected-client');

        currentSessionId = sessionId; // Cập nhật currentSessionId
        clearChatBox(); // Xóa tin nhắn cũ
        loadChatHistory(sessionId); // Tải lại tin nhắn của sessionId được chọn
    };
    clientList.appendChild(clientItem);
}

// Khi trang tải lại, lấy thông tin từ localStorage và hiển thị
window.onload = function () {
    const savedClients = JSON.parse(localStorage.getItem('clientList')) || [];
    savedClients.forEach(client => {
        addClientToList(client.clientId, client.sessionId);
    });
}

function loadChatHistory(sessionId) {
    fetch('/api/chat/messages/' + sessionId)
        .then(response => response.json())
        .then(messages => {
            clearChatBox(); // Xóa nội dung cũ
            messages.forEach(chatMessage => {
                if (chatMessage.sender === "Staff") {
                    displaySent(chatMessage.content);
                } else {
                    displayReceived(chatMessage.content);
                }
            });
        })
        .catch(error => console.error('Error fetching chat history:', error));
}

function unsubscribeFromTopic(sessionId) {
    if (subscriptions.has(sessionId)) {
        subscriptions.get(sessionId).unsubscribe(); // Ngắt kết nối
        subscriptions.delete(sessionId); // Xóa khỏi Map
    }

    // Tìm và xóa clientItem tương ứng trong client-list
    const clientList = document.querySelector('.client-list');
    const clientItem = clientList.querySelector(`[data-session-id="${sessionId}"]`);
    if (clientItem) {
        clientList.removeChild(clientItem);
    }

    if (subscriptions.size === 0) {
        currentSessionId = null;
    }

    const clients = JSON.parse(localStorage.getItem('clientList')) || [];
    const updatedClients = clients.filter(client => client.sessionId !== sessionId);
    localStorage.setItem('clientList', JSON.stringify(updatedClients));
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
    var elem = document.getElementById('chatBox');
    elem.scrollTop = elem.scrollHeight;
}

// Hàm xóa tin nhắn cũ khi chọn một session khác
function clearChatBox() {
    var chatBox = document.getElementById('chatBox');
    while (chatBox.firstChild) {
        chatBox.removeChild(chatBox.firstChild);
    }
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

