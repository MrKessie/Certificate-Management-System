function sendMessage() {
    const message = document.getElementById('message-input').value;
    const senderId = 1;  // Set dynamically based on the logged-in user
    const recipientId = 2;  // Set dynamically based on the administrator user

    if (message.trim() !== "") {
        fetch('/messages/send', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({
                senderId: senderId,
                recipientId: recipientId,
                content: message
            })
        }).then(response => {
            if (response.ok) {
                document.getElementById('message-input').value = '';  // Clear input field
                loadMessages();  // Refresh chat box with new messages
            }
        });
    }
}

// Function to load messages
function loadMessages() {
    const recipientId = 2;  // Dynamically set the recipient's user ID

    fetch(`/messages/history/1/${recipientId}`)  // Sender ID is hardcoded for demo purposes
        .then(response => response.json())
        .then(messages => {
            const chatBox = document.getElementById('messages');
            chatBox.innerHTML = '';  // Clear previous messages

            messages.forEach(message => {
                const senderName = message.sender.name;  // Get sender name dynamically
                chatBox.innerHTML += `<div><strong>${senderName}:</strong> ${message.content}</div>`;
            });

            // Scroll to the bottom of the chat box
            chatBox.scrollTop = chatBox.scrollHeight;
        });
}

// Automatically load messages when the modal opens
document.getElementById('chatModal').addEventListener('shown.bs.modal', function () {
    loadMessages();
});