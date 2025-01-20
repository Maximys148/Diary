package com.maximys.diary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.maximys.diary.dto.EventDTO;
import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.dto.MessageDTO;
import com.maximys.diary.entity.Email;
import com.maximys.diary.entity.Message;
import com.maximys.diary.entity.Token;
import com.maximys.diary.entity.User;
import com.maximys.diary.enums.SendStatus;
import com.maximys.diary.service.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/main")
public class MainController {
    Logger logger = LoggerFactory.getLogger(MainController.class);
    @Autowired
    private final DiaryService diaryService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final EmailService emailService;
    @Autowired
    private final MessageService messageService;
    @Autowired
    private final TokenService tokenService;
    @Autowired
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public MainController(DiaryService diaryService, UserService userService, EmailService emailService, MessageService messageService, TokenService tokenService, NotificationService notificationService, ObjectMapper objectMapper) {
        this.diaryService = diaryService;
        this.userService = userService;
        this.emailService = emailService;
        this.messageService = messageService;
        this.tokenService = tokenService;
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }
    @GetMapping(value = "/main")
    public ModelAndView showMainForm(ModelAndView model, HttpSession session) throws JsonProcessingException {
        Long id = getUserFromToken(session).getId();
        objectMapper.writeValueAsString(diaryService.getEventsByUserId(id));
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        model.addObject("notifications", notificationService.getUpcomingNotifications(getUserFromToken(session)));
        model.addObject("events", objectMapper.writeValueAsString(diaryService.getEventsByUserId(id)));
        model.addObject("currentTime", LocalDateTime.now());
        model.setViewName("main");
        return model;
    }
    // Проверка почты
    @GetMapping("/email")
    public ModelAndView emailPage(ModelAndView model, @RequestParam(required = false) String selectedEmail, HttpSession session) {
        // Логика для получения всех доступных почтовых ящиков
        User user = getUserFromToken(session);
        List<Email> emailList = user.getEmails(); // Ваш сервис для получения адресов

        model.addObject("emails", emailList);

        // Проверка на изменённое значение selectedEmail
        if (selectedEmail != null && !selectedEmail.isEmpty()) {
            // Если почта выбрана, получить сообщения
            List<Message> messages = messageService.getAllMessagesForEmail(selectedEmail); // Ваш метод для получения сообщений
            model.addObject("userEmail", selectedEmail);
            model.addObject("messages", messages);
        } else {
            // Здесь можно сообщить пользователю, что он еще не выбрал почту
            model.addObject("userEmail", null);
            model.addObject("messages", Collections.emptyList());
        }

        model.setViewName("email"); // имя JSP-страницы
        return model;
    }

    @PostMapping("/email")
    public String sendMessage(MessageDTO messageDTO) {
        // Добавьте бизнес-логику для добавления события
        Message message = messageService.createMessage(messageDTO);
        message.setSendStatus(SendStatus.SENDING);
        if(emailService.sendMessage(message)){
            return "redirect:/main/email";
        };
        return "Ошибка";
    }

    // Просмотр профиля
    @GetMapping("/profile")
    public ModelAndView viewProfile(ModelAndView model, HttpSession session) {
        model.addObject("user", getUserFromToken(session));
        model.setViewName("profile");
        return model;
    }
    @PostMapping("/profile")
    public String registerUser(String email, HttpSession session) {
        User user = getUserFromToken(session);
        if(userService.createAndLinkEmailToUser(user.getNickName(), email)){
            logger.info(user.getNickName() + ", успешно обновил профиль");
            return "redirect:/main/profile";
        }
        logger.error(user.getNickName() + ", не смог обновить профиль");
        return"redirect:/main/profile";
    }


    // Добавление события
    @GetMapping("/event")
    public ModelAndView viewEvent(ModelAndView model, HttpSession session) {
        model.addObject("events", diaryService.getEventsByUserId(getUserFromToken(session).getId()));
        model.setViewName("event");
        return model;
    }
    @PostMapping("/event")
    public String addEvent(EventDTO eventDTO, HttpSession session) {;
        eventDTO.setUser(getUserFromToken(session));
        if(diaryService.addEvent(eventDTO)){
            notificationService.createNotifications(eventDTO);
            return "redirect:/main/event";
        };
        return "Ошибка";
    }

    private User getUserFromToken(HttpSession session) {
        Token userToken = (Token) session.getAttribute("token");
        Optional<User> userByToken = tokenService.getUserByToken(userToken);
        return userByToken.orElse(null);
    }
}

