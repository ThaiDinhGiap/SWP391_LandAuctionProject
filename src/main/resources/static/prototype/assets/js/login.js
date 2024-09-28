document.addEventListener('DOMContentLoaded', function() {
    const usernameForm = document.getElementById('usernameForm');

    usernameForm.onsubmit = function(event) {
        event.preventDefault();
        const username = document.getElementById('name').value.trim();
        if (username) {
            localStorage.setItem('username', username);
            window.location.href = "test.html";
        }
    };
});
