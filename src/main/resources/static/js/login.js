document.getElementById('loginForm').addEventListener('submit', function (event) {
    event.preventDefault();

    var form = event.target;
    var data = {
        username: form.username.value,
        password: form.password.value
    };

    fetch('/users/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            } else if (response.ok) {
                window.location.href = "/chat";
            } else {
                return response.json().then(data => {
                    var messageElement = document.getElementById('message');
                    messageElement.textContent = data.message || 'Login failed';
                    messageElement.style.color = 'red';
                });
            }
        })
        .catch(error => {
            var messageElement = document.getElementById('message');
            messageElement.textContent = 'An error occurred';
            messageElement.style.color = 'red';
            console.error('Error:', error);
        });
});
