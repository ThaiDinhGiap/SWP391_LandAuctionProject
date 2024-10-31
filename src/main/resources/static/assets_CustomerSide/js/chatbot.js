// Khởi tạo WebSocket connection và stompClient
var socket;
var stompClient;
var currentSessionId = null;
let inactivityTimer;

function initChat(){
    fetchMainTopics();
}

document.getElementById("init").addEventListener("click", showChatBot);
var cbot = document.getElementById("chat-box");

function showChatBot() {
    var chatbox = document.getElementById('chatbox');
    var initButton = document.getElementById('init');

    // Lấy vị trí của nút init
    var rect = initButton.getBoundingClientRect();
    var originX = rect.left + rect.width / 2;
    var originY = rect.top + rect.height / 2;

    // Tính toán điểm bắt đầu cho transform-origin (theo vị trí của nút init)
    chatbox.style.transformOrigin = `${originX}px ${originY}px`;

    if (chatbox.classList.contains('show')) {
        chatbox.classList.remove('show');
    } else {
        chatbox.classList.add('show');
        // Kiểm tra localStorage xem có sessionId không
        const storedSessionId = localStorage.getItem('currentSessionId');
        if (storedSessionId) {
            currentSessionId = storedSessionId;
            loadChatHistory(currentSessionId); // Khôi phục lại lịch sử chat
            setupWebSocket(currentSessionId);
            directMode(); // Chuyển vào direct mode
        } else {
            fetchMainTopics(); // Nếu không có sessionId, bắt đầu từ main topics
        }
    }
}

function fetchMainTopics() {
    cbot.innerHTML = '';
    fetch('/api/chatbot/topics')
        .then(response => response.json())
        .then(data => {
            displayOptions(data, "main topics");
        });
}

function loadChatHistory(sessionId) {
    fetch('/api/chat/messages/' + sessionId)
        .then(response => response.json())
        .then(messages => {
            clearChatBox(); // Xóa nội dung cũ
            messages.forEach(chatMessage => {
                if (chatMessage.sender === "Client") {
                    displaySentMessage(chatMessage.content);
                } else {
                    displayReceivedMessage(chatMessage.content);
                }
            });
        })
        .catch(error => console.error('Error fetching chat history:', error));
}

function displayOptions(options, type) {
    options.forEach(option => {
        var opt = document.createElement("span");
        opt.innerHTML = option.topicName;
        opt.setAttribute("class", "opt");
        opt.dataset.id = option.topicId;
        opt.dataset.type = type;
        opt.addEventListener("click", handleOpt);
        cbot.appendChild(opt);
        handleScroll();
    });
}

function handleOpt() {
    var selectedOptionId = this.dataset.id;
    var selectedOptionName = this.innerText;
    var type = this.dataset.type;

    var replyElm = document.createElement("p");
    replyElm.setAttribute("class", "rep");
    replyElm.innerHTML = selectedOptionName;
    cbot.appendChild(replyElm);

    // Xóa tất cả các options cũ
    document.querySelectorAll(".opt").forEach(el => el.remove());

    // Gửi request tới server để lấy subtopics hoặc câu hỏi
    // Gửi request tới server để lấy subtopics, câu hỏi, hoặc nhân viên
    fetch(`/api/chatbot/topics/${selectedOptionId}`)
        .then(response => response.json())
        .then(data => {
            if (data.type === 'topics') {
                displayOptions(data.data, "subtopics");
            } else if (data.type === 'questions') {
                displayQuestionList(data.data);
            } else if (data.type === 'Direct Support') {  // Trường hợp hiển thị danh sách nhân viên
                displayStaffList(data.data);
            }
        });
}

function displayQuestionList(questions) {
    questions.forEach(question => {
        var questionElm = document.createElement("span");
        questionElm.innerHTML = `<strong>Q:</strong> ${question.question}`;
        questionElm.setAttribute("class", "opt");
        questionElm.dataset.id = question.questionId;  // Gán questionId cho data attribute
        questionElm.addEventListener("click", function () {
            displayAnswer(question.answer, this);
        });
        cbot.appendChild(questionElm);
        handleScroll();
    });
}

function displayAnswer(answer, questionElm) {
    // Xóa phần tử cũ nếu đã có
    var existingAnswer = questionElm.nextElementSibling;
    if (existingAnswer && existingAnswer.classList.contains("msg")) {
        existingAnswer.remove();
    }

    var answerElm = document.createElement("p");
    answerElm.innerHTML = `<strong>A:</strong> ${answer}`;
    answerElm.setAttribute("class", "msg");
    cbot.insertBefore(answerElm, questionElm.nextSibling);
    handleScroll();
}

function displayStaffList(staffList) {
    staffList.forEach(staff => {
        var staffElm = document.createElement("span");
        staffElm.innerHTML = `<strong>Staff:</strong> ${staff.fullName} (${staff.gender}) - ${staff.email}`;
        staffElm.setAttribute("class", "opt");
        staffElm.dataset.id = staff.staffId;
        staffElm.addEventListener("click", function () {
            confirmConnection(staff);
        });
        cbot.appendChild(staffElm);
        handleScroll();
    });
}

function confirmConnection(staff) {
    var confirmModal = document.getElementById("confirmModal");
    var confirmMessage = document.getElementById("confirmMessage");
    var confirmApprove = document.getElementById("confirmApprove");
    var confirmReject = document.getElementById("confirmReject")

    // Thiết lập tin nhắn xác nhận
    confirmMessage.innerText = `Do you want to connect with ${staff.fullName}?`;

    // Hiển thị modal
    confirmModal.style.display = "flex";

    // Xử lý khi người dùng chọn "Approve"
    confirmApprove.onclick = function() {
        sendConnectionRequest(staff);
        confirmModal.style.display = "none";
    };

    // Xử lý khi người dùng chọn "Reject"
    confirmReject.onclick = function() {
        confirmModal.style.display = "none";
    };
}

function sendConnectionRequest(staff) {
    var contactMessage = document.createElement("p");
    contactMessage.setAttribute("class", "rep");
    contactMessage.innerHTML = `<strong>Contacting:</strong> ${staff.fullName}`;
    cbot.appendChild(contactMessage);
    handleScroll();

    // Gửi yêu cầu đến Staff qua API
    fetch(`/api/chat/connect/${staff.staffId}?clientId=${clientId}`, { method: 'POST' })
        .then(response => response.json())
        .then(data => {
            // Xử lý trạng thái pending
            if (data.status === 'Pending') {
                displayMessage("Đang chờ phản hồi từ Staff...");
                handleStaffResponse(clientId);
            } else if (data.status === 'Rejected'){
                displayMessage("Staff này đang bận, vui lòng chọn Staff khác.")
            }
        });
}

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
                localStorage.setItem('currentSessionId', currentSessionId);
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
        resetInactivityTimer();
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
        stompClient.send("/app/chat.cancelSession", {}, JSON.stringify({
            sender: "Client",
            content: "End Session",
            sessionId: currentSessionId // Gửi tin nhắn dựa trên sessionId
        }));
        stompClient.disconnect();
        localStorage.removeItem('currentSessionId');
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
    initChat();
}

function clearChatBox() {
    var chatBox = document.getElementById('chat-box');
    while (chatBox.firstChild) {
        chatBox.removeChild(chatBox.firstChild);
    }
}

function resetInactivityTimer() {
    clearTimeout(inactivityTimer);
    inactivityTimer = setTimeout(() => {
        disconnectWebSocket();
    }, 30 * 60 * 1000); // 30 phút
}

// window.onload = function () {
//     socket = new SockJS('/ws');
//     stompClient = Stomp.over(socket);
//     stompClient.connect();
// }

// Gọi hàm unsubscribe toàn bộ khi staff thoát khỏi giao diện hoặc đăng xuất
// window.onbeforeunload = function () {
//     disconnectWebSocket();
// };


