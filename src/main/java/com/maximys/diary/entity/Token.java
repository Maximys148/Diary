package com.maximys.diary.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_token")
public class Token extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime expirationTime;

    private String name; // Добавлено поле name

    public Long getId() {
        return id;

    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getName() { // Геттер для name
        return name;
    }

    public void setName(String name) { // Сеттер для name
        this.name = name;
    }
}