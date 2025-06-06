package com.maximys.diary.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.maximys.diary.dto.MessageDTO;
import com.maximys.diary.enums.SendStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "message")
@Data
@NoArgsConstructor
public class Message extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Enumerated(EnumType.STRING)
    private SendStatus sendStatus;

    // Отправитель сообщения — управляемая сторона (будет сериализоваться)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_email_id", nullable = false)
    @JsonManagedReference // или оставить без аннотации, если не нужно включать отправителя в сериализацию
    private Email sender;

    // Получатели сообщения — управляемая сторона (будут сериализоваться)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "message_recipients",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "recipient_email_id")
    )
    @JsonManagedReference // или оставить без аннотации, если не нужно включать получателей в сериализацию
    private List<Email> recipients;

    public Message(MessageDTO messageDTO) {
        this.content = messageDTO.getContent();
        this.sendStatus = messageDTO.getSendStatus();
        // Можно добавить установку sender и recipients из DTO при необходимости
    }
}