function loadNotifications() {
    $.ajax({
        url: '/api/notifications/get?clientId=' + clientId, // Truyền clientId vào URL
        method: 'GET',
        success: function (data) {
            var notificationList = $('#notification-list');
            notificationList.empty(); // Xóa các thông báo cũ
            resetNotificationCount();

            if (data.length > 0) {
                data.forEach(function (notification) {
                    var listItemClass = notification.readStatus === 'unread' ? 'notification-unread' : 'notification-read';
                    var listItem = `<li class="dropdown-item ${listItemClass}" data-id="${notification.notificationId}" onclick="markAsRead(this)">${notification.content}</li>`;
                    notificationList.append(listItem);
                    if (notification.readStatus === 'unread') {
                        updateNotificationCount(1); // Tăng số lượng chưa đọc
                    }
                });
            } else {
                notificationList.append('<li class="dropdown-item notification-read">No notifications</li>');
            }
        },
        error: function (error) {
            alert("Can not load notification");
        }
    });
}

function markAsRead(element) {
    var notificationId = $(element).data('id'); // Lấy notificationId từ data attribute
    $.ajax({
        url: '/api/notifications/markAsRead', // URL API để đánh dấu thông báo đã đọc
        method: 'POST',
        data: { notificationId: notificationId },
        success: function (response) {
            // Cập nhật trạng thái trong giao diện sau khi thành công
            $(element).removeClass('notification-unread').addClass('notification-read');
        },
        error: function (error) {
            alert("Can not mark notification as read");
        }
    });
}

function connectSSE() {
    var eventSource = new EventSource('/api/notifications/stream/' + clientId);

    eventSource.addEventListener('newNotification', function (event) {
        var notification = JSON.parse(event.data);
        var notificationList = $('#notification-list');
        var listItemClass = notification.readStatus === 'unread' ? 'notification-unread' : 'notification-read';
        var listItem = `<li class="dropdown-item ${listItemClass}" data-id="${notification.notificationId}" onclick="markAsRead(event, this)">${notification.content}</li>`;
        notificationList.prepend(listItem); // Thêm thông báo mới lên đầu danh sách
        updateNotificationCount(1);
    });

    eventSource.onerror = function () {
        console.error("Error connecting to SSE stream");
        eventSource.close();
    };
}

// Gọi hàm connectSSE khi load trang
$(document).ready(function () {
    connectSSE();
    countNotifies();
});

function markAsRead2(element) {
    var notificationId = $(element).data('id'); // Correctly access the 'data-id' attribute
    console.log(notificationId);
    $.ajax({
        url: '/api/notifications/markAsRead', // Existing backend endpoint
        method: 'POST',
        data: { notificationId: notificationId },
        success: function (response) {
            // Update the UI to reflect the "read" status
            $(element).closest('tr').find('td:nth-child(3)').text('read').removeClass('text-danger').addClass('text-muted');
            $(element).remove(); // Remove the "Mark as Read" button after marking as read
        },
        error: function (error) {
            alert("Unable to mark notification as read");
        }
    });
}

function updateNotificationCount(increment) {
    var countElement = $('#notification-count');
    var currentCount = parseInt(countElement.text(), 10);
    countElement.text(currentCount + increment);
    countElement.show(); // Hiển thị badge khi có thông báo chưa đọc
}

function resetNotificationCount() {
    $('#notification-count').text('0').hide(); // Ẩn badge khi không có thông báo chưa đọc
}

function countNotifies() {
    $.ajax({
        url: '/api/notifications/get?clientId=' + clientId, // Truyền clientId vào URL
        method: 'GET',
        success: function (data) {
            var notificationList = $('#notification-list');
            notificationList.empty(); // Xóa các thông báo cũ
            resetNotificationCount();

            if (data.length > 0) {
                data.forEach(function (notification) {
                    if (notification.readStatus === 'unread') {
                        updateNotificationCount(1); // Tăng số lượng chưa đọc
                    }
                });
            }
        },
    });
}
