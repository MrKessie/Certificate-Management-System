//package com.cms.Service;
//
//import com.cms.Enum.ConversationStatus;
//import com.cms.Enum.MessageStatus;
//import com.cms.Enum.MessageType;
//import com.cms.Model.Conversation;
//import com.cms.Model.Message;
//import com.cms.Model.User;
//import com.cms.Repository.ConversationRepository;
//import com.cms.Repository.MessageRepository;
//import com.cms.Repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class MessageService {
//    @Autowired
//    private MessageRepository messageRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ConversationRepository conversationRepository;
//
//    // Method to start a new conversation
//    public Conversation startConversation(Integer senderId, Integer receiverId) {
//        // Check if the conversation already exists
//        Optional<Conversation> existingConversation = conversationRepository.findByParticipants(senderId, receiverId);
//
//        if (existingConversation.isPresent()) {
//            return existingConversation.get();
//        } else {
//            // Create a new conversation if it doesn't exist
//            Conversation newConversation = new Conversation();
//            newConversation.setInitiator(userRepository.findById(senderId)
//                    .orElseThrow(() -> new RuntimeException("Sender not found")));
//            newConversation.setResponder(userRepository.findById(receiverId)
//                    .orElseThrow(() -> new RuntimeException("Receiver not found")));
//            newConversation.setStatus(ConversationStatus.ACTIVE);
//            newConversation.setStartedAt(LocalDateTime.now());
//
//            return conversationRepository.save(newConversation);
//        }
//    }
//
//    // Method to send a message in a conversation
//    public Message sendMessage(Integer senderId, Integer receiverId, Integer conversationId, String content, MessageType messageType) {
//        User sender = userRepository.findById(senderId)
//                .orElseThrow(() -> new RuntimeException("Sender not found"));
//        User receiver = userRepository.findById(receiverId)
//                .orElseThrow(() -> new RuntimeException("Receiver not found"));
//
//        Conversation conversation = conversationRepository.findById(conversationId)
//                .orElseThrow(() -> new RuntimeException("Conversation not found"));
//
//        Message message = new Message();
//        message.setSender(sender);
//        message.setReceiver(receiver);
//        message.setConversation(conversation);
//        message.setContent(content);
//        message.setMessageType(messageType);
//        message.setSentAt(LocalDateTime.now());
//
//        return messageRepository.save(message);
//    }
//
//    // Method to retrieve messages between users in a conversation
//    public List<Message> getMessagesBetweenUsers(Integer senderId, Integer receiverId, Integer conversationId) {
//        Conversation conversation = conversationRepository.findById(conversationId)
//                .orElseThrow(() -> new RuntimeException("Conversation not found"));
//
//        return messageRepository.findByConversation(conversation)
//                .stream()
//                .filter(message -> (message.getSender().getUserId() == (senderId) && message.getReceiver().getUserId() == (receiverId)) ||
//                        (message.getSender().getUserId() == (receiverId) && message.getReceiver().getUserId() == (senderId)))
//                .collect(Collectors.toList());
//    }
//
//    // Method to retrieve all messages for a conversation
//    public List<Message> getMessagesByConversation(Integer conversationId) {
//        Conversation conversation = conversationRepository.findById(conversationId)
//                .orElseThrow(() -> new RuntimeException("Conversation not found"));
//
//        return messageRepository.findByConversation(conversation);
//    }
//}
