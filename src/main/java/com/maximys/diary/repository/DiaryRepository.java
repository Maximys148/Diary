package com.maximys.diary.repository;

import com.maximys.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, String> {
}
