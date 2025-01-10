package com.maximys.diary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.maximys.diary.dto.EventDTO;
import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.entity.User;
import com.maximys.diary.service.DiaryService;
import com.maximys.diary.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/main")
public class MainController {
    @Autowired
    private DiaryService diaryService;
    @Autowired
    private UserService userService;
    private ObjectMapper objectMapper;

    public MainController(DiaryService diaryService, UserService userService, ObjectMapper objectMapper) {
        this.diaryService = diaryService;
        this.userService = userService;
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
    public ModelAndView checkEmail(ModelAndView model) {
        // Добавьте бизнес-логику для проверки почты
        model.setViewName("checkEmail");
        return model;
    }

    // Просмотр профиля
    @GetMapping("/profile")
    public ModelAndView viewProfile(ModelAndView model, HttpSession session) {
        // Добавьте бизнес-логику для отображения профиля
        LoginDTO loginDTO = (LoginDTO) session.getAttribute("user");
        User userInfo = userService.getUser(loginDTO);
        model.addObject("nickName", userInfo.getNickName());
        model.addObject("firstName", userInfo.getFirstName());
        model.addObject("lastName", userInfo.getLastName());
        model.addObject("middleName", userInfo.getMiddleName());
        model.setViewName("profile");
        return model;
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
