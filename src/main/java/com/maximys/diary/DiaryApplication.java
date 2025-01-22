package com.maximys.diary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;



// TODO Сделать Scheduler для удаления лишних токенов
// TODO добавить liquibase для user
// TODO Сделать уведомления для почты
// TODO Подкоректировать уведомления для событий
@SpringBootApplication
public class DiaryApplication {
    public static void main(String[] args) {
        System.out.println(new Date());
        SpringApplication.run(DiaryApplication.class, args);
    }
}