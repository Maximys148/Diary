package com.maximys.diary.repository;

import com.maximys.diary.entity.Message;
import com.maximys.diary.enums.SendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> { // Лучше использовать Long для ID

    // Находит сообщения по адресу получателя (работает, если связь настроена правильно)
    List<Message> findByRecipients_Address(String address);

    // Находит сообщения по статусу
    List<Message> findBySendStatus(SendStatus sendStatus);

    // Обновляет статус сообщений для конкретного email
    @Modifying
    @Query("UPDATE Message m SET m.sendStatus = :newStatus " +
            "WHERE m.sendStatus = :oldStatus " +
            "AND EXISTS (SELECT r FROM m.recipients r WHERE r.address = :email)")
    void updateSendStatusByEmail(
            @Param("newStatus") SendStatus newStatus,
            @Param("oldStatus") SendStatus oldStatus,
            @Param("email") String email
    );

    // Находит сообщения по адресу и статусу
    List<Message> findByRecipients_AddressAndSendStatus(String address, SendStatus sendStatus);
}