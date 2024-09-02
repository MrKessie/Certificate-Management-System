package com.cms.Service;

import com.cms.Enum.MessageStatus;
import com.cms.Enum.MessageType;
import com.cms.Model.Conversation;
import com.cms.Model.Message;
import com.cms.Model.User;
import com.cms.Repository.ConversationRepository;
import com.cms.Repository.MessageRepository;
import com.cms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    // Send a new message in a conversation
    public Message sendMessage(Integer senderId, Integer receiverId, Integer conversationId, String content, MessageType messageType) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setConversation(conversation);
        message.setContent(content);
        message.setMessageType(messageType);
        message.setStatus(MessageStatus.SENT);
        message.setTimestamp(new Timestamp(System.currentTimeMillis()));

        return messageRepository.save(message);
    }

    // Get all messages in a conversation
    public List<Message> getMessagesByConversation(Integer conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        return messageRepository.findByConversation(conversation);
    }

    // Mark a message as read
    public void markMessageAsRead(Integer messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        message.setRead(true);
        message.setStatus(MessageStatus.READ);
        messageRepository.save(message);
    }

    // Get unread messages for a specific user (receiver)
    public List<Message> getUnreadMessagesForUser(Integer receiverId) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        return messageRepository.findByReceiverAndIsReadFalse(receiver);
    }

    // Get messages between two users in a conversation
    public List<Message> getMessagesBetweenUsers(Integer senderId, Integer receiverId, Integer conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        // Fetch all messages in this conversation
        return messageRepository.findByConversation(conversation)
                .stream()
                .filter(message ->
                        (message.getSender().getUserId() == senderId && message.getReceiver().getUserId() == receiverId) ||
                                (message.getSender().getUserId() == receiverId && message.getReceiver().getUserId() == senderId))
                .collect(Collectors.toList());
    }
}
