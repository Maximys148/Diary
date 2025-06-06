package com.maximys.diary.service;

import com.maximys.diary.dto.EventDTO;
import com.maximys.diary.entity.Event;
import com.maximys.diary.repository.DiaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiaryService {
    private DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public List<Event> getEventsByUserId(Long userId){
        if(diaryRepository.findAllByUserId(userId) != null){
            return diaryRepository.findAllByUserId(userId);
        }
        return null;
    }

    public boolean addEvent(EventDTO eventDTO){
        diaryRepository.save(new Event(eventDTO));
        return true;
    }

}
