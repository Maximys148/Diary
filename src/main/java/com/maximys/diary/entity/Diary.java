package com.maximys.diary.entity;

import com.maximys.diary.dto.EventDTO;
import com.maximys.diary.enums.UnitTime;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "diary")
public class Diary extends TimeEntity {
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

    public Diary(User user, String name, String data, Date dateTime, int reminderFrequency, int leadTime, UnitTime unitTime) {
        this.user = user;
        this.name = name;
        this.data = data;
        this.dateTime = dateTime;
        this.reminderFrequency = reminderFrequency;
        this.leadTime = leadTime;
        this.unitTime = unitTime;
    }

    public Diary(EventDTO eventDTO) {
        this.user = eventDTO.getUser();
        this.name = eventDTO.getName();
        this.data = eventDTO.getData();
        this.dateTime = eventDTO.getDateTime();
        this.reminderFrequency = eventDTO.getReminderFrequency();
        this.leadTime = eventDTO.getLeadTime();
        this.unitTime = eventDTO.getUnitTime();
    }

    public Diary() {
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getReminderFrequency() {
        return reminderFrequency;
    }

    public void setReminderFrequency(int reminderFrequency) {
        this.reminderFrequency = reminderFrequency;
    }

    public int getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(int leadTime) {
        this.leadTime = leadTime;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public UnitTime getUnitTime() {
        return unitTime;
    }

    public void setUnitTime(UnitTime unitTime) {
        this.unitTime = unitTime;
    }
}