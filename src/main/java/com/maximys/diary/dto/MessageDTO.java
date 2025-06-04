package com.maximys.diary.dto;

import com.maximys.diary.enums.SendStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String content; // Содержимое сообщения
    private SendStatus sendStatus; // Статус отправки
    private String senderEmail; // Email отправителя
    private String recipientEmails; // Список адресов получателей


}