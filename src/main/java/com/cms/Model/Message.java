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
    private int messageId;

    @ManyToOne
    @JoinColumn(name = "sender_id", foreignKey = @ForeignKey(name = "fk_message_sender",
            foreignKeyDefinition = "FOREIGN KEY (sender_id) REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", foreignKey = @ForeignKey(name = "fk_message_receiver",
            foreignKeyDefinition = "FOREIGN KEY (receiver_id) REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "conversation_id", foreignKey = @ForeignKey(name = "fk_message_conversation",
            foreignKeyDefinition = "FOREIGN KEY (conversation_id) REFERENCES conversation(conversation_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private Conversation conversation;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private MessageType messageType;  // TEXT, IMAGE, FILE (using Enum)

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "timestamp")
    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MessageStatus status;  // SENT, DELIVERED, READ (using Enum)

    @Column(name = "is_read")
    private boolean isRead;

    public Message() {
        this.sentAt = LocalDateTime.now();
        this.status = MessageStatus.SENT;
        this.isRead = false;
    }


    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
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

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
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
