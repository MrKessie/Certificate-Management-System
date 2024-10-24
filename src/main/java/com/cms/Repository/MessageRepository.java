//package com.cms.Repository;
//
//import com.cms.Model.Conversation;
//import com.cms.Model.Message;
//import com.cms.Model.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//@Repository
//public interface MessageRepository extends JpaRepository<Message, Integer> {
//    List<Message> findByConversation(Conversation conversation);
//
//    List<Message> findByReceiverAndIsReadFalse(User receiver);
//}
