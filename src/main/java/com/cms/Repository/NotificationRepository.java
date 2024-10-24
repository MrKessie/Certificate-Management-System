//package com.cms.Repository;
//
//import com.cms.Model.Notification;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface NotificationRepository extends JpaRepository<Notification, Integer> {
//    // Find notifications by user (administrator) and status
//    List<Notification> findByUserIdAndStatus(int userId, String status);
//
//    // Find all unread notifications for a specific user
//    List<Notification> findByUserIdAndStatusIsUnread(int userId);
//
//    // Find notifications related to a specific message
//    List<Notification> findByMessageId(int messageId);
//}
