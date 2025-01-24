package com.maximys.diary.repository;

import com.maximys.diary.entity.Message;
import com.maximys.diary.enums.SendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByRecipients_Address(String address);

    // Метод для получения всех сообщений со статусом SEND
    List<Message> findBySendStatus(SendStatus sendStatus);

    // Метод для замены статуса сообщений на READ
    @Modifying
    @Query("UPDATE Message m SET m.sendStatus = :newStatus WHERE m.sendStatus = :oldStatus AND EXISTS (SELECT 1 FROM m.recipients r WHERE r.address = :email)")
    void updateSendStatusByEmail(@Param("newStatus") SendStatus newStatus,
                                 @Param("oldStatus") SendStatus oldStatus,
                                 @Param("email") String email);
    List<Message> findByRecipients_AddressAndSendStatus(String address, SendStatus sendStatus);
}