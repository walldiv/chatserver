'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');


var stompClient = null;
var username = null;
var myChannel = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

//uses SockJS and stomp client to connect to the /ws endpoint that we configured in Spring Boot.
function connect(event) {
    username = document.querySelector('#name').value.trim();

    if (username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

//Upon successful connection, the client subscribes to /topic/public destination and tells the user’s name
//to the server by sending a message to the /app/chat.addUser destination.
function onConnected() {
    stompClient.subscribe('/app/chat.public', function (message) {
        alert(message);
    }, { 'username': username });

    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/chat.public', onMessageReceived);
    // Tell your username to the server
    stompClient.send('/app/chat.addUser.public',
        {},
        JSON.stringify({ sender: username, type: 'JOIN' })
    )
    myChannel = "public";
    connectingElement.classList.add('hidden');
}

//CHANGE THE CHANNELS/TOPICS FROM SELECT OPTIONS LIST
function changeChatChannel(channel) {
    console.log("changeChatChannel()");
    myChannel = channel;
    //SUBSCRIPTION TO CHANNEL FOR RETRIEVAL OF PERSONS LIST
    stompClient.subscribe('/app/chat.public', function (message) {
        alert(message);
    }, { 'username': username });
    //JOIN CHANNEL BROADCAST TO CHATROOM
    stompClient.send(`/app/chat.addUser.${myChannel}`,
        {},
        JSON.stringify({ sender: username, type: 'JOIN' })
    )
    connectingElement.classList.add('hidden');
    // alert("CHANGED CHAT CHANNEL TO " + channel);
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
        // alert(`SENDING MESSAGE TO: ${myChannel}`);
        stompClient.send(`/app/chat.sendMessage.${myChannel}`, {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

//adds the person to the peoples list locally when a message is
//received that a person has joined channel
function addPersonToPeopleList(person) {
    alert("AddingPersonToPeopleList FIRED!!!");
    var peopleList = document.getElementById('peopleList');

    var messageElement = document.createElement('li');
    messageElement.classList.add('chat-message');
    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode(person.sender[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(person.sender);

    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode(person.sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);

    peopleList.appendChild(messageElement);
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if (message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
        addPersonToPeopleList(message);
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
