package com.cms.Service;

import com.cms.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.management.Notification;

@Service
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    NotificationRepository notificationRepository;

    public void notifyAdmins(String message, int messageId) {
        // Send notification to all administrators through WebSocket
        template.convertAndSend("/topic/adminNotifications", message);

        // Optionally, store notification in the database
//        notificationRepository.save(new Notification(messageId, "Unread"));
    }
}
