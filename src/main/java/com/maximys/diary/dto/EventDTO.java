package com.maximys.diary.dto;

import com.maximys.diary.entity.User;
import com.maximys.diary.enums.UnitTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private String name;
    private String data;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dateTime;
    private int reminderFrequency; // Частота напоминания (например, каждый день, неделю и т.д.)
    private int leadTime; // За какое время до события напоминать
    private UnitTime unitTime;
    private User user;

}
