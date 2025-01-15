package com.maximys.diary.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "email")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;

    @ManyToOne
    @JoinColumn(name = "receiver_email_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL) // Изменено на "sender"
    @JsonIgnore // Игнорируем, чтобы избежать циклической сериализации
    private List<Message> messages; // Теперь корректно ссылается на отправленные сообщения

    public Email(String address, User user) {
        this.address = address;
        this.user = user;
    }

    public Email() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}

