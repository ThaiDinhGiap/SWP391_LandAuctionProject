document.addEventListener("DOMContentLoaded", function () {
    // Load danh sách main topics
    fetch("/api/chatbot/topics")
        .then(response => response.json())
        .then(data => {
            const mainTopicsTable = document.querySelector('#mainTopics');
            data.forEach(topic => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${topic.topicId}</td>
                    <td id="topicName-${topic.topicId}">${topic.topicName}</td>
                    <td>
                        <button id="viewButton-${topic.topicId}" onclick="viewDetails(${topic.topicId})">View</button>
                        <button id="editButton-${topic.topicId}" onclick="editDetails(${topic.topicId})">Edit</button>
                        <button id="deleteButton-${topic.topicId}" onclick="deleteTopic(${topic.topicId})">Delete</button>
                        <button id="saveButton-${topic.topicId}" onclick="saveDetails(${topic.topicId})" style="display: none;">Save</button>
                    </td>
                `;
                mainTopicsTable.appendChild(row);
            });
        })
        .catch(error => console.error('Error fetching main topics:', error));
});

function viewDetails(topicId) {
    // Kiểm tra xem đã có bảng chi tiết cho topicId này chưa
    const existingDetailsDiv = document.getElementById(`detailsDiv-${topicId}`);

    if (existingDetailsDiv) {
        // Nếu đã có, xóa div khỏi DOM
        existingDetailsDiv.remove();
        return;
    }

    fetch(`/api/chatbot/view/${topicId}`)
        .then(response => response.json())
        .then(data => {
            // Tạo một div mới cho subtopics hoặc questions
            const subTopicsContainer = document.getElementById('subTopicsContainer');
            const newDetailsDiv = document.createElement('div');
            newDetailsDiv.id = `detailsDiv-${topicId}`;
            newDetailsDiv.className = 'chatbot-settings-container';

            if (data.type === "topics") {
                newDetailsDiv.innerHTML = `
                    <div class="table-header">
                        <h3>Sub-Topics for Topic ID: ${topicId}</h3>
                        <button onclick="deleteDetails(${topicId})">Close</button>
                    </div>
                    <table id="detailsTable-${topicId}">
                        <thead class="text-primary">
                        <tr>
                            <th>TOPIC ID</th>
                            <th>TOPIC NAME</th>
                            <th>ACTIONS</th>
                        </tr>
                        </thead>
                        <tbody id="subTopicsOrQuestions-${topicId}">
                        <!-- Subtopic sẽ được load ở đây -->
                        </tbody>
                    </table>
                `;
            } else if (data.type === "questions") {
                newDetailsDiv.innerHTML = `
                    <div class="table-header">
                        <h3>Question List for Topic ID: ${topicId}</h3>
                        <button onclick="deleteDetails(${topicId})">Close</button>
                    </div>
                    <table id="detailsTable-${topicId}">
                        <thead class="text-primary">
                        <tr>
                            <th>QUESTION ID</th>
                            <th>QUESTION</th>
                            <th>ANSWER</th>
                            <th>ACTIONS</th>
                        </tr>
                        </thead>
                        <tbody id="subTopicsOrQuestions-${topicId}">
                        <!-- Questions sẽ được load ở đây -->
                        </tbody>
                    </table>
                `;
            }

            subTopicsContainer.appendChild(newDetailsDiv);

            // Lấy tbody tương ứng để thêm nội dung subtopic hoặc question
            const detailsTable = document.querySelector(`#subTopicsOrQuestions-${topicId}`);
            detailsTable.innerHTML = ''; // Xóa nội dung cũ

            if (data.type === "topics") {
                data.data.forEach(subTopic => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${subTopic.topicId}</td>
                        <td id="topicName-${subTopic.topicId}">${subTopic.topicName}</td>
                        <td>
                            <button id="viewButton-${subTopic.topicId}" onclick="viewDetails(${subTopic.topicId})">View</button>
                            <button id="editButton-${subTopic.topicId}" onclick="editDetails(${subTopic.topicId})">Edit</button>
                            <button id="deleteButton-${subTopic.topicId}" onclick="deleteTopic(${subTopic.topicId})">Delete</button>
                            <button id="saveButton-${subTopic.topicId}" style="display:none;" onclick="saveDetails(${subTopic.topicId})">Save</button>
                        </td>
                    `;
                    detailsTable.appendChild(row);
                });
            } else if (data.type === "questions") {
                data.data.forEach(question => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${question.questionId}</td>
                        <td id="questionText-${question.questionId}">${question.question}</td>
                        <td id="answerText-${question.questionId}">${question.answer}</td>
                        <td>
                            <button id="editButtonQuestion-${question.questionId}" onclick="editQuestion(${question.questionId})">Edit</button>
                            <button id="deleteButtonQuestion-${question.questionId}" onclick="deleteQuestion(${question.questionId})">Delete</button>
                            <button id="saveButtonQuestion-${question.questionId}" style="display:none;" onclick="saveQuestion(${question.questionId})">Save</button>
                        </td>
                    `;
                    detailsTable.appendChild(row);
                });
            }

            // Hiển thị div mới chứa bảng chi tiết
            newDetailsDiv.style.display = 'block';
        })
        .catch(error => console.error('Error fetching details:', error));
}

// Hàm chỉnh sửa topic name
function editDetails(topicId) {
    const topicNameCell = document.getElementById(`topicName-${topicId}`);
    const currentName = topicNameCell.textContent;
    topicNameCell.innerHTML = `<textarea id="editInput-${topicId}" class="editable-textarea">${currentName}</textarea>`;

    // Hiện nút Save, vô hiệu hóa nút Edit và View
    document.getElementById(`editButton-${topicId}`).style.display = 'none';
    document.getElementById(`viewButton-${topicId}`).disabled = true;
    document.getElementById(`saveButton-${topicId}`).style.display = 'inline';
}

// Hàm lưu thay đổi topic name
function saveDetails(topicId) {
    const newName = document.getElementById(`editInput-${topicId}`).value;

    fetch(`/api/chatbot/topics/update/${topicId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ topicName: newName })
    })
        .then(response => {
            if (response.ok) {
                // Cập nhật giao diện với tên mới
                const topicNameCell = document.getElementById(`topicName-${topicId}`);
                topicNameCell.textContent = newName;  // Hiển thị giá trị mới dưới dạng văn bản, không còn là input nữa

                // Khôi phục trạng thái của các nút
                document.getElementById(`editButton-${topicId}`).style.display = 'inline';
                document.getElementById(`viewButton-${topicId}`).disabled = false;
                document.getElementById(`saveButton-${topicId}`).style.display = 'none';
            } else {
                console.error('Error saving topic name');
            }
        })
        .catch(error => console.error('Error saving topic name:', error));
}

function editQuestion(questionId) {
    // Lấy các ô chứa nội dung của câu hỏi và câu trả lời
    const questionTextCell = document.getElementById(`questionText-${questionId}`);
    const answerTextCell = document.getElementById(`answerText-${questionId}`);
    const currentQuestion = questionTextCell.textContent;
    const currentAnswer = answerTextCell.textContent;

    // Tạo các ô input để người dùng nhập giá trị mới cho câu hỏi và câu trả lời
    // Tạo các ô textarea để người dùng nhập giá trị mới cho câu hỏi và câu trả lời
    questionTextCell.innerHTML = `<textarea id="editQuestionInput-${questionId}" class="editable-textarea">${currentQuestion}</textarea>`;
    answerTextCell.innerHTML = `<textarea id="editAnswerInput-${questionId}" class="editable-textarea">${currentAnswer}</textarea>`;

    // Ẩn nút Edit và hiện nút Save
    document.getElementById(`editButtonQuestion-${questionId}`).style.display = 'none';
    document.getElementById(`saveButtonQuestion-${questionId}`).style.display = 'inline';
}

function saveQuestion(questionId) {
    // Lấy giá trị mới từ các ô input
    const questionInput = document.getElementById(`editQuestionInput-${questionId}`);
    const answerInput = document.getElementById(`editAnswerInput-${questionId}`);

    // Kiểm tra nếu không tìm thấy phần tử
    if (!questionInput || !answerInput) {
        console.error('Không tìm thấy phần tử textarea để lấy giá trị.');
        return;
    }

    const newQuestion = questionInput.value;
    const newAnswer = answerInput.value;

    // Gửi yêu cầu cập nhật lên server
    fetch(`/api/chatbot/questions/update/${questionId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ question: newQuestion, answer: newAnswer })
    })
        .then(response => {
            if (response.ok) {
                // Cập nhật giao diện với giá trị mới sau khi cập nhật thành công
                document.getElementById(`questionText-${questionId}`).textContent = newQuestion;
                document.getElementById(`answerText-${questionId}`).textContent = newAnswer;

                // Khôi phục trạng thái của các nút
                document.getElementById(`editButtonQuestion-${questionId}`).style.display = 'inline';
                document.getElementById(`saveButtonQuestion-${questionId}`).style.display = 'none';
            } else {
                console.error('Error saving question');
            }
        })
        .catch(error => console.error('Error saving question:', error));
}

function deleteDetails(topicId) {
    const detailsDiv = document.getElementById(`detailsDiv-${topicId}`);
    if (detailsDiv) {
        detailsDiv.remove();
    }
}

function deleteTopic(topicId) {
    fetch(`/api/chatbot/topics/delete/${topicId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (response.ok) {
                // Xóa hàng của topic khỏi bảng
                const row = document.getElementById(`deleteButton-${topicId}`).closest('tr');
                row.remove();
                console.log("Topic deleted successfully");
            } else {
                console.error("Failed to delete topic");
            }
        })
        .catch(error => console.error("Error deleting topic:", error));
}

function deleteQuestion(questionId) {
    fetch(`/api/chatbot/questions/delete/${questionId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (response.ok) {
                // Xóa hàng của question khỏi bảng
                const row = document.getElementById(`deleteButtonQuestion-${questionId}`).closest('tr');
                row.remove();
                console.log("Question deleted successfully");
            } else {
                console.error("Failed to delete question");
            }
        })
        .catch(error => console.error("Error deleting question:", error));
}
