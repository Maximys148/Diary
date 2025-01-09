package com.maximys.diary.service;

import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.dto.RegistrationDTO;
import com.maximys.diary.entity.User;
import com.maximys.diary.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DiaryService {
    private DiaryService diaryService;

    public DiaryService(DiaryService diaryService) {
        this.diaryService = diaryService;
    }


}
