package com.maximys.diary.controller;

import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.dto.RegistrationDTO;
import com.maximys.diary.entity.User;
import com.maximys.diary.service.DiaryService;
import com.maximys.diary.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/main")
public class MainController {
    @Autowired
    private DiaryService diaryService;
    private UserService userService;

    public MainController(DiaryService diaryService, UserService userService) {
        this.diaryService = diaryService;
        this.userService = userService;
    }
    @GetMapping(value = "/main")
    public ModelAndView showMainForm(ModelAndView model) {
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
    public ModelAndView addEvent(ModelAndView model) {
        // Добавьте бизнес-логику для добавления события
        model.setViewName("addEvent");
        return model;
    }
}
