package com.maximys.diary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.crypto.Data;
import java.util.Date;

// TODO посмотреть что такое RegExp как им пользоваться, у пользователя должна быть двусторонняя связь с почтой, сделать статус обработки у сообщения(в отправке, получено, прочитано)
// TODO сделать время создания у сообщения(когда сообщение было создано, обновлено)(Date), создать отдельную сущность в entity, которая будет хранить в себе дату обновления, дату создания
// TODO добавить в Diary дату напоминания и частоту напоминания и за какое время до события напоминать


//  TODO создать сущность от которой будут наследоваться другие сущности(которые будут работать с Data) и нужно будет прописать аннотацию для полей Data
//  TODO шифровать пароли(MD5) для User
//  TODO использовать crone выражения для частоты напоминания
//  TODO сделать чтобы каждый пользователь знал SendStatus так как получателей может быть несколько (придётся создать новую таблицу где будет зависимоть )
@SpringBootApplication
public class DiaryApplication {
    public static void main(String[] args) {
        System.out.println(new Date());
        SpringApplication.run(DiaryApplication.class, args);
    }
}