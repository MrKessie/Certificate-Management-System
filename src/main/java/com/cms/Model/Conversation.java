package com.cms.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;  // The user who started the conversation (could be a student or employer)

    @ManyToOne
    @JoinColumn(name = "responder_id", referencedColumnName = "id")
    private User responder;  // The user who responded to the conversation (could be an admin)

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "is_active")
    private boolean isActive;

    // Getters and Setters
    public Conversation() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.isActive = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
