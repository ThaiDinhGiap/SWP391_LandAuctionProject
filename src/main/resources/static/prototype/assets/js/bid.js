document.addEventListener('DOMContentLoaded', function() {
    var username = localStorage.getItem('username');
    if (!username) {
        window.location.href = "login_test.html";
        return;
    }

    var socket = new SockJS('/ws');
    var stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);

    function onConnected() {
        // Subscribe to the Public Topic
        stompClient.subscribe('/topic/public', onMessageReceived);

        // Notify server that the user has joined
        stompClient.send("/app/chat.addUser", {}, JSON.stringify({ sender: username, type: 'JOIN' }));
    }

    function onError(error) {
        document.getElementById('userStatusMessage').textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    }

    // Functionality for placing a bid
    document.getElementById('placeBidButton').onclick = function() {
        var bidAmount = document.getElementById('bidAmount').value.trim();
        if (stompClient && bidAmount && !isNaN(bidAmount)) {
            var bidMessage = {
                sender: username,
                content: bidAmount,
                type: 'BID'
            };
            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(bidMessage));
            document.getElementById('bidAmount').value = ''; // Clear input field after sending
        }
    };

    function addBidHistory(bidder, amount, date) {
        var row = document.createElement('tr');
        row.innerHTML = `<td>${bidder}</td><td>${amount}</td><td>${date}</td>`;
        document.getElementById('biddingHistory').appendChild(row);
    }

    function updateCurrentBid(amount) {
        document.getElementById('currentBid').innerText = `${amount}`;
    }

    // Adjust bid amount
    document.getElementById('decreaseBid').onclick = function() {
        var bidAmount = parseFloat(document.getElementById('bidAmount').value) || 0;
        if (bidAmount > 0) {
            document.getElementById('bidAmount').value = (bidAmount - 1000).toFixed(2);
        }
    };

    document.getElementById('increaseBid').onclick = function() {
        var bidAmount = parseFloat(document.getElementById('bidAmount').value) || 0;
        document.getElementById('bidAmount').value = (bidAmount + 1000).toFixed(2);
    };

    // Handle incoming bid messages
    function onMessageReceived(payload) {
        var message = JSON.parse(payload.body);

        if (message.type === 'BID') {
            addBidHistory(message.sender, message.content, new Date().toLocaleString());
            updateCurrentBid(message.content);
        }
    }
});
