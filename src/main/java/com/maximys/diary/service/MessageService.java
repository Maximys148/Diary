package com.maximys.diary.service;

import com.maximys.diary.dto.MessageDTO;
import com.maximys.diary.entity.Message;
import com.maximys.diary.entity.User;
import com.maximys.diary.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    @Autowired
    private EmailService emailService;


    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }
    public List<Message> getAllMessagesForEmail(String address){
        return messageRepository.findByRecipients_Address(address);
    }

    public Message createMessage(MessageDTO messageDTO) {
        Message message = new Message(messageDTO);
        // Теперь устанавливаем recipients через emailService
        message.setRecipients(emailService.findEmailsByString(messageDTO.getRecipientEmails()));
        message.setSender(emailService.findEmailByString(messageDTO.getSenderEmail()));
        // Здесь вы можете сохранить message через ваш репозиторий

        return message;
    }
}
