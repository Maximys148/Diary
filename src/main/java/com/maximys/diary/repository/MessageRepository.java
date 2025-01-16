package com.maximys.diary.repository;

import com.maximys.diary.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByRecipients_Address(String address);
}