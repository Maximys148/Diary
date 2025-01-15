package com.maximys.diary.entity;

import com.maximys.diary.enums.SendStatus;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "message")
public class Message extends TimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @Enumerated(EnumType.STRING)
    private SendStatus sendStatus;

    @ManyToOne
    @JoinColumn(name = "sender_email_id", nullable = false)
    private Email sender; // Email отправителя

    @ManyToMany // Связь "Многие к Многим" с Email
    @JoinTable(
            name = "message_recipients", // Имя промежуточной таблицы
            joinColumns = @JoinColumn(name = "message_id"), // Колонка для связи с message
            inverseJoinColumns = @JoinColumn(name = "recipient_email_id") // Колонка для связи с получателями
    )
    private List<Email> recipients; // Список адресатов

    public Message() {
    }

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

    public List<Email> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Email> recipients) {
        this.recipients = recipients;
    }
}
