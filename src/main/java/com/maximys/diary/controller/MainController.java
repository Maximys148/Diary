package com.maximys.diary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.maximys.diary.dto.EventDTO;
import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.dto.MessageDTO;
import com.maximys.diary.entity.Email;
import com.maximys.diary.entity.Message;
import com.maximys.diary.entity.User;
import com.maximys.diary.enums.SendStatus;
import com.maximys.diary.service.DiaryService;
import com.maximys.diary.service.EmailService;
import com.maximys.diary.service.MessageService;
import com.maximys.diary.service.UserService;
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

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping(value = "/main")
public class MainController {
    Logger logger = LoggerFactory.getLogger(MainController.class);
    @Autowired
    private DiaryService diaryService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MessageService messageService;
    private ObjectMapper objectMapper;

    public MainController(DiaryService diaryService, UserService userService, EmailService emailService, MessageService messageService, ObjectMapper objectMapper) {
        this.diaryService = diaryService;
        this.userService = userService;
        this.emailService = emailService;
        this.messageService = messageService;
        this.objectMapper = objectMapper;
    }
    @GetMapping(value = "/main")
    public ModelAndView showMainForm(ModelAndView model, HttpSession session) throws JsonProcessingException {
        LoginDTO loginDTO = (LoginDTO) session.getAttribute("user");
        Long id = userService.getUser(loginDTO).getId();
        objectMapper.writeValueAsString(diaryService.getEventsByUserId(id));
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        model.addObject("events", objectMapper.writeValueAsString(diaryService.getEventsByUserId(id)));
        model.setViewName("main");
        return model;
    }
    // Проверка почты
    @GetMapping("/email")
    public ModelAndView emailPage(ModelAndView model, @RequestParam(required = false) String selectedEmail, HttpSession session) {
        // Логика для получения всех доступных почтовых ящиков
        LoginDTO loginDTO = (LoginDTO) session.getAttribute("user");
        User user = userService.getUser(loginDTO);
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
    public String sendMessage(MessageDTO messageDTO, HttpSession session) {
        // Добавьте бизнес-логику для добавления события
        LoginDTO loginDTO = (LoginDTO) session.getAttribute("user");
        User user = userService.getUser(loginDTO);
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
        // Добавьте бизнес-логику для отображения профиля
        LoginDTO loginDTO = (LoginDTO) session.getAttribute("user");
        User userInfo = userService.getUser(loginDTO);
        model.addObject("user", userInfo);
        model.setViewName("profile");
        return model;
    }
    @PostMapping("/profile")
    public String registerUser(String email, HttpSession session) {
        LoginDTO loginDTO = (LoginDTO) session.getAttribute("user");
        User userInfo = userService.getUser(loginDTO);
        if(userService.createAndLinkEmailToUser(userInfo.getNickName(), email)){
            logger.info(loginDTO.getNickName() + ", успешно обновил профиль");
            return "redirect:/main/profile";
        }
        logger.error(loginDTO.getNickName() + ", не смог обновить профиль");
        return"redirect:/main/profile";
    }


    // Добавление события
    @GetMapping("/event")
    public ModelAndView viewEvent(ModelAndView model, HttpSession session) {
        LoginDTO loginDTO = (LoginDTO) session.getAttribute("user");
        Long id = userService.getUser(loginDTO).getId();
        model.addObject("events", diaryService.getEventsByUserId(id));
        model.setViewName("event");
        return model;
    }
    @PostMapping("/event")
    public String addEvent(EventDTO eventDTO, HttpSession session) {
        // Добавьте бизнес-логику для добавления события
        LoginDTO loginDTO = (LoginDTO) session.getAttribute("user");
        User user = userService.getUser(loginDTO);
        eventDTO.setUser(user);
        if(diaryService.addEvent(eventDTO)){
            return "redirect:/main/event";
        };
        return "Ошибка";
    }
}
