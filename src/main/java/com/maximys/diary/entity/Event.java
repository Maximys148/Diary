package com.maximys.diary.entity;

import com.maximys.diary.dto.EventDTO;
import com.maximys.diary.enums.UnitTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "diary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String name;
    private String data;
    private Date dateTime; // Дата запланированного события
    private int reminderFrequency; // Частота напоминания (например, каждый день, неделю и т.д.)
    private int leadTime; // За какое время до события напоминать
    @Enumerated(EnumType.STRING)
    private UnitTime unitTime; // Единица измерения

    public Event(EventDTO eventDTO) {
        this.user = eventDTO.getUser();
        this.name = eventDTO.getName();
        this.data = eventDTO.getData();
        this.dateTime = eventDTO.getDateTime();
        this.reminderFrequency = eventDTO.getReminderFrequency();
        this.leadTime = eventDTO.getLeadTime();
        this.unitTime = eventDTO.getUnitTime();
    }
}