package com.maximys.diary.service;

import com.maximys.diary.dto.MessageDTO;
import com.maximys.diary.entity.Email;
import com.maximys.diary.entity.Message;
import com.maximys.diary.repository.EmailRepository;
import com.maximys.diary.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {
    private EmailRepository emailRepository;
    private MessageRepository messageRepository;
    public EmailService(EmailRepository emailRepository, MessageRepository messageRepository){
        this.emailRepository = emailRepository;
        this.messageRepository = messageRepository;
    }

    public boolean sendMessage(Message message) {
        // Получаем адреса электронной почты получателей извлекая их из списка message
        List<Email> recipientEmails = message.getRecipients();

        // Проверяем, что все получатели существуют
        for (Email email : recipientEmails) {
            if (!emailRepository.existsByAddress(email.getAddress())) {
                return false; // Если хотя бы один email не существует, возвращаем false
            }
        }

        // Сохраняем сообщение для каждого получателя
        for (Email email : recipientEmails) {
            Email byAddress = emailRepository.findByAddress(email.getAddress());
            List<Message> messages = byAddress.getReceivedMessages(); // мб надо менять метод
            messages.add(message); // Используем уже существующий объект message
            byAddress.setReceivedMessages(messages); // мб надо менять метод
            emailRepository.save(byAddress); // Сохраняем обновленный объект Email
        }

        // В конце метода можно возвращать true, если сообщения были успешно отправлены
        return true;
    }
    public List<Email> findEmailsByString(String emailString){
        List<String> emailStringList = new ArrayList<>();

        // Проверка на пустую строку
        if (emailString == null || emailString.trim().isEmpty()) {
            return null; // Возвращаем пустой список
        }

        // Разбиваем строку по запятым
        String[] emailsArray = emailString.split(",");

        // Используем цикл for для добавления адресов в список
        for (String email : emailsArray) {
            // Убираем пробелы вокруг адреса и добавляем в список
            emailStringList.add(email.trim());
        }
        List<Email> emailList = new ArrayList<>();
        for (String address : emailStringList) {
            emailList.add(emailRepository.findByAddress(address)); // Создаем объект Email и добавляем в список
        }
        return emailList;
    }

    public Email findEmailByString(String emailString){
        return emailRepository.findByAddress(emailString);
    }
}
