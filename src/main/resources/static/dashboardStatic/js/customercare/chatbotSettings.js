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
                        <button id="saveButton-${topic.topicId}" onclick="saveDetails(${topic.topicId})" style="display: none;">Save</button>
                    </td>
                `;
                mainTopicsTable.appendChild(row);
            });
        })
        .catch(error => console.error('Error fetching main topics:', error));
});

// Hàm load subtopics hoặc questions
function viewDetails(topicId) {
    fetch(`/api/topics/chatbot/${topicId}`)
        .then(response => response.json())
        .then(data => {
            const detailsTable = document.querySelector('#subTopicsOrQuestions');
            detailsTable.innerHTML = ''; // Xóa nội dung cũ

            if (data.type === "topics") {
                data.data.forEach(subTopic => {
                    const row = document.createElement('tr');
                    row.innerHTML = `<td>${subTopic.topicId}</td><td>${subTopic.topicName}</td>`;
                    detailsTable.appendChild(row);
                });
            } else if (data.type === "questions") {
                data.data.forEach(question => {
                    const row = document.createElement('tr');
                    row.innerHTML = `<td>${question.questionId}</td><td>${question.question}</td>`;
                    detailsTable.appendChild(row);
                });
            }

            document.getElementById('details').style.display = 'block'; // Hiển thị bảng chi tiết
        })
        .catch(error => console.error('Error fetching details:', error));
}

// Hàm chỉnh sửa topic name
function editDetails(topicId) {
    const topicNameCell = document.getElementById(`topicName-${topicId}`);
    const currentName = topicNameCell.textContent;
    topicNameCell.innerHTML = `<input type="text" id="editInput-${topicId}" value="${currentName}">`;

    // Hiện nút Save, vô hiệu hóa nút Edit và View
    document.getElementById(`editButton-${topicId}`).style.display = 'none';
    document.getElementById(`viewButton-${topicId}`).disabled = true;
    document.getElementById(`saveButton-${topicId}`).style.display = 'inline';
}

// Hàm lưu thay đổi topic name
function saveDetails(topicId) {
    const newName = document.getElementById(`editInput-${topicId}`).value;

    fetch(`/api/chatbot/topics/${topicId}`, {
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
                topicNameCell.textContent = newName;

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
