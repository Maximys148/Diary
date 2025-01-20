package com.maximys.diary.repository;

import com.maximys.diary.entity.Notification;
import com.maximys.diary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByUser(User user);
}
