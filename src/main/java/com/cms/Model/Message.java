package com.cms.Model;

import com.cms.Enum.MessageStatus;
import com.cms.Enum.MessageType;
import jakarta.persistence.*;

import java.awt.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "conversation_id", referencedColumnName = "id")
    private Conversation conversation;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private MessageType messageType;  // TEXT, IMAGE, FILE (using Enum)

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MessageStatus status;  // SENT, DELIVERED, READ (using Enum)

    @Column(name = "is_read")
    private boolean isRead;

    public Message() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.status = MessageStatus.SENT;
        this.isRead = false;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
