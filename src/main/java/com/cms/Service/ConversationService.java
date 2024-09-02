package com.cms.Service;

import com.cms.Model.Conversation;
import com.cms.Model.User;
import com.cms.Repository.ConversationRepository;
import com.cms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    public Conversation createNewConversation(int initiatorId) {
        User initiator = userRepository.findById(initiatorId);

        Conversation conversation = new Conversation();
        conversation.setInitiator(initiator);

        return conversationRepository.save(conversation);
    }
}
