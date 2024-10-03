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

    // Xóa class selected khỏi tất cả các topic
    document.querySelectorAll('.opt').forEach(function(el) {
        el.classList.remove('selected');
    });

    // Thêm class selected vào topic được chọn
    this.classList.add('selected');

    var replyElm = document.createElement("p");
    replyElm.setAttribute("class", "rep");
    replyElm.innerHTML = selectedOptionName;
    cbot.appendChild(replyElm);

    // Xóa tất cả các options cũ
    document.querySelectorAll(".opt").forEach(el => el.remove());

    // Gửi request tới server để lấy subtopics, câu hỏi, hoặc nhân viên
    fetch(`/api/chatbot/topics/${selectedOptionId}`)
        .then(response => response.json())
        .then(data => {
            if (data.type === 'topics') {
                displayOptions(data.data, "subtopics");
            } else if (data.type === 'questions') {
                displayQuestionList(data.data);
            } else if (data.type === 'Direct Support') {
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
            displayStaffDetails(staff);
        });
        cbot.appendChild(staffElm);
        handleScroll();
    });
}

// Hàm để hiển thị chi tiết của nhân viên khi người dùng chọn
function displayStaffDetails(staff) {
    var staffDetailElm = document.createElement("p");
    staffDetailElm.innerHTML = `<strong>Contacting:</strong> ${staff.fullName}`;
    staffDetailElm.setAttribute("class", "msg");
    cbot.appendChild(staffDetailElm);
    handleScroll();
}


function handleScroll() {
    var elem = document.getElementById('chat-box');
    elem.scrollTop = elem.scrollHeight;
}
