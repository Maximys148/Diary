package com.maximys.diary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.crypto.Data;
import java.util.Date;

// TODO посмотреть что такое RegExp как им пользоваться, у пользователя должна быть двусторонняя связь с почтой, сделать статус обработки у сообщения(в отправке, получено, прочитано)
// TODO сделать время создания у сообщения(когда сообщение было создано, обновлено)(Date), создать отдельную сущность в entity, которая будет хранить в себе дату обновления, дату создания
// TODO добавить в Diary дату напоминания и частоту напоминания и за какое время до события напоминать
@SpringBootApplication
public class DiaryApplication {
    public static void main(String[] args) {
        System.out.println(new Date());
        SpringApplication.run(DiaryApplication.class, args);
    }
}