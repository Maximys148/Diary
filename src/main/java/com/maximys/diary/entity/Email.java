package com.maximys.diary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // Сообщения, где этот email является отправителем
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> sentMessages;

    // Сообщения, где этот email является получателем
    @ManyToMany(mappedBy = "recipients")
    private List<Message> receivedMessages;

    public Email(User user, String address) {
        this.user = user;
        this.address = address;
    }
}