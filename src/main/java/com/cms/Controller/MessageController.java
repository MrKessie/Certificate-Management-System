package com.cms.Controller;

import com.cms.Enum.MessageType;
import com.cms.Model.Conversation;
import com.cms.Model.Message;
import com.cms.Repository.ConversationRepository;
import com.cms.Repository.MessageRepository;
import com.cms.Service.MessageService;
import com.cms.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    // Endpoint to send a new message
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(
            @RequestParam int senderId,
            @RequestParam int receiverId,
            @RequestParam int conversationId,
            @RequestParam String content,
            @RequestParam MessageType messageType) {

        Message message = messageService.sendMessage(senderId, receiverId, conversationId, content, messageType);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    // Endpoint to get all messages in a conversation
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<Message>> getMessagesByConversation(@PathVariable int conversationId) {
        List<Message> messages = messageService.getMessagesByConversation(conversationId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // Endpoint to get unread messages for a specific user
    @GetMapping("/unread/{receiverId}")
    public ResponseEntity<List<Message>> getUnreadMessages(@PathVariable int receiverId) {
        List<Message> unreadMessages = messageService.getUnreadMessagesForUser(receiverId);
        return new ResponseEntity<>(unreadMessages, HttpStatus.OK);
    }

    // Endpoint to mark a message as read
    @PostMapping("/read/{messageId}")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable int messageId) {
        messageService.markMessageAsRead(messageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint to get messages between two users in a conversation
    @GetMapping("/between")
    public ResponseEntity<List<Message>> getMessagesBetweenUsers(
            @RequestParam int senderId,
            @RequestParam int receiverId,
            @RequestParam int conversationId) {

        List<Message> messages = messageService.getMessagesBetweenUsers(senderId, receiverId, conversationId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // Endpoint to start a new conversation and send the first message
    @PostMapping("/start")
    public ResponseEntity<Message> startConversation(
            @RequestParam int senderId,
            @RequestParam int receiverId,
            @RequestParam String content,
            @RequestParam MessageType messageType) {

        Conversation conversation = messageService.startConversation(senderId, receiverId);
        Message message = messageService.sendMessage(senderId, receiverId, conversation.getId(), content, messageType);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
}
