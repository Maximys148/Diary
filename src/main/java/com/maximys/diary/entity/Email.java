package com.maximys.diary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_email")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_id")
    @SequenceGenerator(name = "email_id", sequenceName = "email_id", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(unique = true, nullable = false)
    private String address;

    // Список отправленных сообщений — игнорируем при сериализации, чтобы избежать цикла
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Message> sentMessages;

    // Получатели сообщений — игнорируем при сериализации
    @ManyToMany(mappedBy = "recipients")
    @JsonIgnore
    private List<Message> receivedMessages;

    public Email(User user, String address) {
        this.user = user;
        this.address = address;
    }
}