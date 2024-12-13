package com.maximys.diary.service;

import com.maximys.diary.repository.EmailRepository;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private EmailRepository emailRepository;
    public EmailService(EmailRepository emailRepository){
        this.emailRepository = emailRepository;
    }
}
