package com.maximys.diary.service;

import com.maximys.diary.repository.DiaryRepository;
import org.springframework.stereotype.Service;

@Service
public class DiaryService {
    private DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }
}
