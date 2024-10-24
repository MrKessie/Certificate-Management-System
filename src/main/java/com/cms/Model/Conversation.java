package com.cms.Model;

import com.cms.Enum.ConversationStatus;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int conversationId;

    @ManyToOne
    @JoinColumn(name = "initiator_id", foreignKey = @ForeignKey(name = "fk_conversation_initiator",
            foreignKeyDefinition = "FOREIGN KEY (initiator_id) REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "responder_id", foreignKey = @ForeignKey(name = "fk_conversation_responder",
            foreignKeyDefinition = "FOREIGN KEY (responder_id) REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private User responder;

    @Enumerated(EnumType.STRING)
    private ConversationStatus status;

    @Column(name = "created_at")
    private LocalDateTime startedAt;

    @Column(name = "is_active")
    private boolean isActive;

    // Getters and Setters
    public Conversation() {
        this.startedAt = LocalDateTime.now();
        this.isActive = true;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public User getInitiator() {
        return initiator;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
    }

    public User getResponder() {
        return responder;
    }

    public void setResponder(User responder) {
        this.responder = responder;
    }

    public ConversationStatus getStatus() {
        return status;
    }

    public void setStatus(ConversationStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
