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
        fetchMainTopics();
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

function handleChat(message) {
    var elm = document.createElement("p");
    elm.innerHTML = message;
    elm.setAttribute("class", "msg");
    cbot.appendChild(elm);
    handleScroll();
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

function displayMessage(message) {
    var messageElement = document.createElement('p');
    messageElement.textContent = message;
    messageElement.setAttribute("class", "msg");
    cbot.appendChild(messageElement);
    handleScroll();
}

function handleScroll() {
    var elem = document.getElementById('chat-box');
    elem.scrollTop = elem.scrollHeight;
}
