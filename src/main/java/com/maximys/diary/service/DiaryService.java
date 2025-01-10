package com.maximys.diary.service;

import com.maximys.diary.dto.EventDTO;
import com.maximys.diary.entity.Diary;
import com.maximys.diary.repository.DiaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiaryService {
    private DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public List<Diary> getEventsByUserId(Long userId){
        if(diaryRepository.findAllByUserId(userId) != null){
            return diaryRepository.findAllByUserId(userId);
        }
        return null;
    }

    public boolean addEvent(EventDTO eventDTO){
        diaryRepository.save(new Diary(eventDTO));
        return true;
    }

}
