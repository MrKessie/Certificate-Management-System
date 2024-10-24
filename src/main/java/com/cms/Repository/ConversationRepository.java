//package com.cms.Repository;
//
//import com.cms.Model.Conversation;
//import com.cms.Model.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
//    // Find all conversations by the user (student or employer) who initiated the conversation
//    @Query("SELECT c FROM Conversation c WHERE (c.initiator.userId = :senderId AND c.responder.userId = :receiverId) OR (c.initiator.userId = :receiverId AND c.responder.userId = :senderId)")
//    Optional<Conversation> findByParticipants(@Param("initiatorId") int senderId, @Param("receiverId") int receiverId);
//}
