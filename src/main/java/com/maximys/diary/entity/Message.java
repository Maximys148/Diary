package com.maximys.diary.entity;

import com.maximys.diary.enums.SendStatus;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "message")
public class Message extends TimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @Enumerated()
    private SendStatus sendStatus;

    @OneToOne
    @JoinColumn(name = "sender_email_id", nullable = false)
    private Email sender;

    @OneToMany
    @JoinColumn(name = "receiver_email_id", nullable = false)
    private List<Email> receiver;

    public SendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

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

    public List<Email> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<Email> receiver) {
        this.receiver = receiver;
    }
}
