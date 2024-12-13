package com.maximys.diary.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne
    @JoinColumn(name = "sender_email_id", nullable = false)
    private Email sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Email receiver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Email getSender() {
        return sender;
    }

    public void setSender(Email sender) {
        this.sender = sender;
    }

    public Email getReceiver() {
        return receiver;
    }

    public void setReceiver(Email receiver) {
        this.receiver = receiver;
    }

}
