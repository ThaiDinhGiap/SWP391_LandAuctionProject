document.getElementById("init").addEventListener("click", showChatBot);
var cbot = document.getElementById("chat-box");

function showChatBot() {
    var chatbox = document.getElementById('chatbox');
    if (chatbox.style.display === 'none' || chatbox.style.display === '') {
        chatbox.style.display = 'block';
        document.getElementById('init').innerText = 'CLOSE CHAT';
        fetchMainTopics();  // Fetch main topics khi mở chat
    } else {
        chatbox.style.display = 'none';
        document.getElementById('init').innerText = 'START CHAT';
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
    fetch(`/api/chatbot/topics/${selectedOptionId}`)
        .then(response => response.json())
        .then(data => {
            if (data.type === 'topics') {
                displayOptions(data.data, "subtopics");
            } else if (data.type === 'questions') {
                displayQuestionList(data.data);
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

function handleScroll() {
    var elem = document.getElementById('chat-box');
    elem.scrollTop = elem.scrollHeight;
}
