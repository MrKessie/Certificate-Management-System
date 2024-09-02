package com.cms.Repository;

import com.cms.Model.Conversation;
import com.cms.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    // Find all conversations by the user (student or employer) who initiated the conversation
    List<Conversation> findByInitiator(User initiator);
    List<Conversation> findByIsActive(boolean isActive);
}
