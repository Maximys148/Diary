package com.maximys.diary.repository;

import com.maximys.diary.entity.Event;
import com.maximys.diary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Event, String> {
    List<Event> findAllByUserId(Long userId);
}
