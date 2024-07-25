var stompClient = null;
var username = null;

function getUsername() {
    return fetch('/users/nickname')
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to fetch username');
            }
        })
        .then(data => {
            return data.data.nickname; // 응답 구조에 따라 username 가져오기
        })
        .catch(error => {
            console.error('Error:', error);
            return 'Unknown User'; // 실패 시 기본 사용자 이름 설정
        });
}

function connect() {
    getUsername().then(fetchedUsername => {
        username = fetchedUsername;

        var socket = new SockJS('/chat');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/public', function (messageOutput) {
                showMessage(JSON.parse(messageOutput.body));
            });
        });
    });
}

function sendMessage() {
    var messageInput = document.getElementById('messageInput');
    var message = {
        content: messageInput.value,
        sender: username,
        type: 'CHAT'
    };
    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(message));
    messageInput.value = '';
}

function showMessage(message) {
    var messages = document.getElementById('chatMessages');
    var messageElement = document.createElement('div');
    messageElement.className = 'chat-message';
    messageElement.textContent = message.sender + ": " + message.content;
    messages.appendChild(messageElement);
    messages.scrollTop = messages.scrollHeight;
}

connect();
