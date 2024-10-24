//package com.cms.Controller;
//
//import com.cms.Enum.MessageType;
//import com.cms.Model.Conversation;
//import com.cms.Model.Message;
//import com.cms.Model.User;
//import com.cms.Repository.ConversationRepository;
//import com.cms.Repository.MessageRepository;
//import com.cms.Repository.UserRepository;
//import com.cms.Service.MessageService;
//import com.cms.Service.NotificationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.*;
//
//@Controller
//@RequestMapping("/messages")
//public class MessageController {
//    @Autowired
//    private MessageService messageService;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    ConversationRepository conversationRepository;
//
//    // Start a conversation
//    @PostMapping("/start")
//    public ResponseEntity<Conversation> startConversation(
//            @RequestParam int senderId,
//            @RequestParam int receiverId) {
//
//        Conversation conversation = messageService.startConversation(senderId, receiverId);
//        return new ResponseEntity<>(conversation, HttpStatus.CREATED);
//    }
//
//    // Send a message in a conversation
//    @PostMapping("/send")
//    public ResponseEntity<Message> sendMessage(
//            @RequestParam int senderId,
//            @RequestParam int receiverId,
//            @RequestParam int conversationId,
//            @RequestParam String content,
//            @RequestParam MessageType messageType) {
//
//        Message message = messageService.sendMessage(senderId, receiverId, conversationId, content, messageType);
//        return new ResponseEntity<>(message, HttpStatus.CREATED);
//    }
//
//    // Get all messages in a conversation
//    @GetMapping("/conversation/{conversationId}")
//    public ResponseEntity<List<Message>> getMessagesByConversation(@PathVariable int conversationId) {
//        List<Message> messages = messageService.getMessagesByConversation(conversationId);
//        return new ResponseEntity<>(messages, HttpStatus.OK);
//    }
//
//    // Get messages between two users
//    @GetMapping("/between")
//    public ResponseEntity<List<Message>> getMessagesBetweenUsers(
//            @RequestParam int senderId,
//            @RequestParam int receiverId,
//            @RequestParam int conversationId) {
//
//        List<Message> messages = messageService.getMessagesBetweenUsers(senderId, receiverId, conversationId);
//        return new ResponseEntity<>(messages, HttpStatus.OK);
//    }
//
//    @PostMapping("/assign-admin")
//    public ResponseEntity<User> assignAdministrator(@RequestParam Integer conversationId) {
//        List<User> availableAdmins = userRepository.findAvailableAdmins(); // Get list of online admins
//
//        if (availableAdmins.isEmpty()) {
//            throw new RuntimeException("No administrators available");
//        }
//
//        User assignedAdmin = availableAdmins.get(0); // Pick the first available admin (you can customize logic)
//
//        // Update the conversation with the assigned admin
//        Conversation conversation = conversationRepository.findById(conversationId)
//                .orElseThrow(() -> new RuntimeException("Conversation not found"));
//        conversation.setResponder(assignedAdmin);
//        conversationRepository.save(conversation);
//
//        return ResponseEntity.ok(assignedAdmin);
//}
