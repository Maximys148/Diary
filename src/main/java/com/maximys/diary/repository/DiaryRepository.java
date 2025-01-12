package com.maximys.diary.repository;

import com.maximys.diary.entity.Diary;
import com.maximys.diary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, String> {
    List<Diary> findAllByUserId(Long userId);
    User getUserById(Long id);
}
