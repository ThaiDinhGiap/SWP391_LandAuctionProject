document.addEventListener('DOMContentLoaded', function () {
    // Các biến username và auctionId sẽ được định nghĩa trong auctionPage.html
    var socket = new SockJS('/ws');
    var stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);

    function onConnected() {
        document.getElementById("userStatusMessage").hidden=true;

        // Subscribe to the specific auction topic
        stompClient.subscribe('/topic/auction/' + auctionId, onMessageReceived);

        // Notify server that the user has joined
        stompClient.send("/app/chat.addUser", {}, JSON.stringify({sender: username, type: 'JOIN'}));
    }

    function onError(error) {
        document.getElementById("userStatusMessage").hidden=false;
        document.getElementById('userStatusMessage').textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    }

    // Functionality for placing a bid
    document.getElementById('placeBidButton').onclick = function () {
        var bidAmount = document.getElementById('bidAmount').value.trim();
        if (stompClient && bidAmount && !isNaN(bidAmount)) {
            var now = new Date().getTime();
            var endTime2 = new Date(endTime).getTime();
            var timeLeft = endTime2 - now;

            // Kiểm tra nếu left time < extra_time_unit
            if (timeLeft < extraTimeUnit * 1000) { // extra_time_unit ở đơn vị giây
                endTime = new Date(now + extraTimeUnit * 1000).toISOString(); // Reset endTime
                updateCountdown();
            }

            var bidMessage = {
                sender: username,
                content: parseInt(bidAmount),
                type: 'BID',
                auctionId: auctionId
            };
            stompClient.send("/app/bid", {}, JSON.stringify(bidMessage));
            document.getElementById('bidAmount').value = currentHighestPrice + minimumBidIncrement; // Clear input field after sending
        }
    };

    function addBidHistory(sender, bidder, amount, date) {
        var row = document.createElement('tr');

        if (username === sender) {
            displayName = 'Your bid';
            document.getElementById('currentBidName').innerText = 'Current Bid (with you)';
        } else {
            displayName = bidder;
            document.getElementById('currentBidName').innerText = 'Current Bid (not with you)';
        }

        row.innerHTML = `<td>${displayName}</td><td>${amount}₫</td><td>${date}</td>`;
        var biddingHistory = document.getElementById('biddingHistory');

        // Thêm hàng mới vào đầu bảng
        biddingHistory.insertBefore(row, biddingHistory.firstChild);
    }


    function updateCurrentBid(amount) {
        document.getElementById('currentBid').innerText = `${amount}`;
    }

    // Adjust bid amount
    document.getElementById('decreaseBid').onclick = function () {
        var bidAmount = parseInt(document.getElementById('bidAmount').value) || 0;
        if (bidAmount > 0) {
            document.getElementById('bidAmount').value = (bidAmount - minimumBidIncrement);
        }
    };

    document.getElementById('increaseBid').onclick = function () {
        var bidAmount = parseInt(document.getElementById('bidAmount').value) || 0;
        document.getElementById('bidAmount').value = (bidAmount + minimumBidIncrement);
    };

    // Handle incoming bid messages
    function onMessageReceived(payload) {
        var message = JSON.parse(payload.body);

        if (message.type === 'BID') {
            addBidHistory(message.sender, message.nickName, message.content, formattedDate());
            updateCurrentBid(message.content);
        }
    }

    function formattedDate() {
        const date = new Date(); // Đảm bảo rằng bạn sử dụng new Date() đúng cách

        const formattedDate = date.getFullYear() + '-' +
            String(date.getMonth() + 1).padStart(2, '0') + '-' +
            String(date.getDate()).padStart(2, '0') + ' ' +
            String(date.getHours()).padStart(2, '0') + ':' +
            String(date.getMinutes()).padStart(2, '0') + ':' +
            String(date.getSeconds()).padStart(2, '0');

        return formattedDate;
    }

    function updateCountdown() {
        var endTime2 = new Date(endTime).getTime();
        var now = new Date().getTime();
        var timeLeft = endTime2 - now;

        if (timeLeft <= 0) {
            document.getElementById('timeLeft').textContent = 'Auction Ended';
            clearInterval(countdownInterval);
            sendEndAuctionMessage();
        } else {
            var days = Math.floor(timeLeft / (1000 * 60 * 60 * 24));
            var hours = Math.floor((timeLeft % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            var minutes = Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60));
            var seconds = Math.floor((timeLeft % (1000 * 60)) / 1000);
            document.getElementById('timeLeft').textContent = days + "d " + hours + "h " + minutes + "m " + seconds + "s";
        }
    }

    function sendEndAuctionMessage() {
        var endAuctionMessage = {
            auctionId: auctionId,
            typeMessage: 'Completed', // Hoặc 'Cancelled' tùy thuộc vào tình huống
            dealPrice: currentHighestPrice, // Giá cuối cùng đã đấu giá
        };

        stompClient.send("/app/endAuction", {}, JSON.stringify(endAuctionMessage));
    }


    var countdownInterval = setInterval(updateCountdown, 1000);
});
