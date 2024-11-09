function showMainTopicInput() {
    document.getElementById("mainTopicInputContainer").style.display = "block";
    cancelSubTopic();
    cancelQuestion();
}

function showSubTopicInput() {
    document.getElementById("subTopicInputContainer").style.display = "block";
    cancelMainTopic();
    cancelQuestion();
}

function showQuestionInput() {
    document.getElementById("questionInputContainer").style.display = "block";
    cancelMainTopic();
    cancelSubTopic();
}

<!--- ================================================================================= -->
function saveMainTopic() {
    const topicName = document.getElementById("topicName").value;
    if (topicName) {
        fetch("/api/chatbot/insert/mainTopic", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ topicName: topicName })
        })
            .then(response => {
                if (response.ok) {
                    alert("Topic saved successfully!");
                    cancelMainTopic();
                    refreshMainTopics();
                    refreshSubTopics();
                } else {
                    alert("Failed to save the topic. Please try again.");
                }
            })
            .catch(error => {
                console.error('Error saving topic:', error);
                alert("An error occurred while saving the topic.");
            });
    } else {
        alert("Please enter a topic name.");
    }
}

function saveSubTopic() {
    const mainTopicId = document.getElementById("selectMainTopic").value;
    const subTopicName = document.getElementById("subTopicName").value;

    if (mainTopicId && subTopicName) {
        fetch("/api/chatbot/insert/subTopic", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ mainTopicId: mainTopicId, topicName: subTopicName })
        })
            .then(response => {
                if (response.ok) {
                    alert("Sub Topic saved successfully!");
                    cancelSubTopic();
                    refreshSubTopics();
                } else {
                    console.error('Error saving sub topic');
                    alert("Failed to save the sub topic. Please try again.");
                }
            })
            .catch(error => {
                console.error('Error saving sub topic:', error);
                alert("An error occurred while saving the sub topic.");
            });
    } else {
        alert("Please select a main topic and enter a sub topic name.");
    }
}

function saveQuestion() {
    const subTopicId = document.getElementById("selectSubTopic").value;
    const questionText = document.getElementById("questionText").value;
    const answerText = document.getElementById("answerText").value;

    if (subTopicId && questionText && answerText) {
        fetch("/api/chatbot/insert/question", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ subTopicId: subTopicId, question: questionText, answer: answerText })
        })
            .then(response => {
                if (response.ok) {
                    alert("Question saved successfully!");
                    cancelQuestion();
                } else {
                    console.error('Error saving question');
                    alert("Failed to save the question. Please try again.");
                }
            })
            .catch(error => {
                console.error('Error saving question:', error);
                alert("An error occurred while saving the question.");
            });
    } else {
        alert("Please select a subtopic, enter a question, and an answer.");
    }
}
<!--- ================================================================================= -->
function cancelMainTopic() {
    document.getElementById("mainTopicInputContainer").style.display = "none";
    document.getElementById("topicName").value = "";
}

function cancelSubTopic() {
    document.getElementById("subTopicInputContainer").style.display = "none";
    document.getElementById("subTopicName").value = "";
    document.getElementById("selectMainTopic").selectedIndex = 0;
}

function cancelQuestion() {
    document.getElementById("questionInputContainer").style.display = "none";
    document.getElementById("questionText").value = "";
    document.getElementById("answerText").value = "";
    document.getElementById("selectSubTopic").selectedIndex = 0;
}

<!-- ================================================================================================ -->
function refreshMainTopics() {
    fetch("/api/chatbot/get/mainTopics")
        .then(response => response.json())
        .then(data => {
            const selectMainTopic = document.getElementById("selectMainTopic");
            selectMainTopic.innerHTML = "";
            data.forEach(topic => {
                const option = document.createElement("option");
                option.value = topic.topicId;
                option.textContent = topic.topicName;
                selectMainTopic.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading main topics:', error));
}

function refreshSubTopics() {
    fetch("/api/chatbot/get/subTopics")
        .then(response => response.json())
        .then(data => {
            const selectSubTopic = document.getElementById("selectSubTopic");
            selectSubTopic.innerHTML = "";
            data.forEach(subtopic => {
                const option = document.createElement("option");
                option.value = subtopic.topicId;
                option.textContent = subtopic.topicName;
                selectSubTopic.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading sub topics:', error));
}

