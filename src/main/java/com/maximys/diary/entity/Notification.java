package com.maximys.diary.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification") //
@Data
public class Notification extends TimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean read;

    private LocalDateTime alertTime;


}