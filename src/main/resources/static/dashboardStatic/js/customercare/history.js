document.addEventListener("DOMContentLoaded", function () {
    const staffId = 4; // Giả sử staffId đã được định nghĩa và đúng

    console.log("Fetching sessions for staffId:", staffId);

    fetch("/api/chat/history/" + staffId)
        .then(response => {
            if (!response.ok) {
                console.log("Error");
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log("Data received from API:", data);
            const sessionList = document.querySelector('.session-list');
            if (!sessionList) {
                console.error("Session list element not found.");
                return;
            }
            data.forEach(session => {
                const sessionItem = document.createElement('div');
                sessionItem.classList.add('session-item');
                sessionItem.textContent = `Session ID: ${session.sessionId}`;

                // Thêm sự kiện click để chọn session
                sessionItem.addEventListener('click', function () {
                    // Xóa class 'selected' từ các session khác
                    document.querySelectorAll('.session-item').forEach(item => item.classList.remove('selected'));
                    // Thêm class 'selected' cho session được chọn
                    sessionItem.classList.add('selected');
                    // Gọi hàm load tin nhắn cho session này
                    loadChatHistory(session.sessionId);
                });

                sessionList.appendChild(sessionItem);
            });
            console.log("Session items added to the list.");
        })
        .catch(error => console.error('Error fetching session list:', error));

    // Hàm để load tin nhắn của session được chọn
    function loadChatHistory(sessionId) {
        console.log("Fetching messages for sessionId:", sessionId);

        fetch(`/api/chat/messages/${sessionId}`)
            .then(response => response.json())
            .then(data => {
                const chatHistoryBox = document.getElementById('chatHistoryBox');
                chatHistoryBox.innerHTML = ''; // Xóa nội dung cũ

                let previousDate = null; // Biến để lưu ngày của tin nhắn trước đó

                data.forEach(message => {
                    const timestamp = new Date(message.timestamp);
                    const currentDate = timestamp.toLocaleDateString('en-GB', {
                        year: 'numeric',
                        month: 'short',
                        day: 'numeric'
                    }); // Lấy ngày tháng năm không có thứ

                    // Kiểm tra nếu ngày thay đổi
                    if (previousDate !== currentDate) {
                        // Tạo một phần tử ngày mới
                        const dateDiv = document.createElement('div');
                        dateDiv.classList.add('chat-date');
                        dateDiv.textContent = currentDate;
                        chatHistoryBox.appendChild(dateDiv);

                        // Cập nhật ngày trước đó
                        previousDate = currentDate;
                    }

                    const messageWrapper = document.createElement('div');
                    messageWrapper.classList.add('message-wrapper', message.sender === 'Staff' ? 'left' : 'right');

                    const timeDiv = document.createElement('div');
                    const formattedTime = `${timestamp.getHours()}:${timestamp.getMinutes().toString().padStart(2, '0')}`;
                    timeDiv.classList.add('message-time');
                    timeDiv.textContent = formattedTime;

                    const messageDiv = document.createElement('div');
                    messageDiv.classList.add(message.sender === 'Staff' ? 'rep' : 'send');
                    messageDiv.textContent = message.content;

                    if (message.sender === 'Staff') {
                        messageWrapper.appendChild(timeDiv);    // Thời gian ở bên trái tin nhắn
                        messageWrapper.appendChild(messageDiv); // Tin nhắn của Staff
                    } else {
                        messageWrapper.appendChild(messageDiv); // Tin nhắn của Client
                        messageWrapper.appendChild(timeDiv);    // Thời gian ở bên phải tin nhắn
                    }

                    chatHistoryBox.appendChild(messageWrapper);
                });
            })
            .catch(error => console.error('Error fetching chat messages:', error));
    }

});
