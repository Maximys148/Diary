package com.maximys.diary.entity;

import com.maximys.diary.enums.SendStatus;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private SendStatus sendStatus;

    @OneToOne
    @JoinColumn(name = "sender_email_id", nullable = false)
    private Email sender;

    @ManyToOne
    @JoinColumn(name = "receiver_email_id", nullable = false)
    private Email receiver;
    private Date createdDate;
    private Date updatedDate;

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

    public Email getReceiver() {
        return receiver;
    }

    public void setReceiver(Email receiver) {
        this.receiver = receiver;
    }


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
