package com.maximys.diary.service;

import com.maximys.diary.dto.MessageDTO;
import com.maximys.diary.entity.Email;
import com.maximys.diary.entity.Message;
import com.maximys.diary.entity.User;
import com.maximys.diary.enums.SendStatus;
import com.maximys.diary.repository.MessageRepository;
import jakarta.transaction.Transactional;
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

    public int getUnreadMessageCountForEmail(String emailAddress) {
        return messageRepository.findByRecipients_AddressAndSendStatus(emailAddress, SendStatus.SEND).size();
    }

    // Пример метода для обновления статуса сообщений
    @Transactional
    public void markMessagesAsRead(String emailAddress) {
        messageRepository.updateSendStatusByEmail(SendStatus.READ, SendStatus.SEND, emailAddress);
        // Реализуйте логику для обновления всех сообщений, связанных с данным email, на "прочитано"
    }

    @Transactional
    public void updateMessage(Message message) {
        // Предполагая, что message уже содержит обновленные данные
        messageRepository.save(message);
    }
}
