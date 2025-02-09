package com.maximys.diary.dto;

import com.maximys.diary.entity.Diary;
import com.maximys.diary.entity.User;
import com.maximys.diary.enums.UnitTime;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class EventDTO {
    private String name;
    private String data;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dateTime;
    private int reminderFrequency; // Частота напоминания (например, каждый день, неделю и т.д.)
    private int leadTime; // За какое время до события напоминать
    private UnitTime unitTime;
    private User user;

    public EventDTO(String name, String data, Date dateTime, int reminderFrequency, int leadTime, UnitTime unitTime, User user) {
        this.name = name;
        this.data = data;
        this.dateTime = dateTime;
        this.reminderFrequency = reminderFrequency;
        this.leadTime = leadTime;
        this.unitTime = unitTime;
        this.user = user;
    }

    public EventDTO() {
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

    public void setData(String date) {
        this.data = date;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
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

    public UnitTime getUnitTime() {
        return unitTime;
    }

    public void setUnitTime(UnitTime unitTime) {
        this.unitTime = unitTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
