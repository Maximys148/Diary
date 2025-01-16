package com.maximys.diary.dto;

import com.maximys.diary.entity.Email;
import com.maximys.diary.enums.SendStatus;
import com.maximys.diary.service.EmailService;

import java.util.List;


public class MessageDTO {
    private String content; // Содержимое сообщения
    private SendStatus sendStatus; // Статус отправки
    private String senderEmail; // Email отправителя
    private String recipientEmails; // Список адресов получателей
    private EmailService emailService;

    // Конструктор без аргументов
    public MessageDTO() {
    }

    // Конструктор с параметрами
    public MessageDTO(String content, SendStatus sendStatus, String senderEmail, String recipientEmails) {
        this.content = content;
        this.sendStatus = sendStatus;
        this.senderEmail = senderEmail;
        this.recipientEmails = recipientEmails;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getRecipientEmails() {
        return recipientEmails;
    }

    public void setRecipientEmails(String recipientEmails) {
        this.recipientEmails = recipientEmails;
    }
}