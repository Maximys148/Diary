package com.maximys.diary.controller;

import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.dto.RegistrationDTO;
import com.maximys.diary.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/start")
public class AuthRegController {
    @Autowired
    private AuthService authService;

    public AuthRegController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping(value = "/register")
    public ModelAndView showRegistrationForm(ModelAndView model) {
        model.setViewName("register");
        model.addObject("registrationDto", new RegistrationDTO());
        return model;
    }

    @PostMapping("/register")
    public String registerUser(RegistrationDTO registrationDto) {
        if(authService.saveUser(registrationDto)){
            return "redirect:/start/login";
        }
        return "Пользователь с таким никнеймом уже есть";
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm(ModelAndView model){
        model.setViewName("login");
        model.addObject("loginDto", new LoginDTO());
        return model;
    }

    @PostMapping("/login")
    public String loginUser(LoginDTO loginDto, HttpSession session) {
        if (authService.login(loginDto)) {
            session.setAttribute("user", loginDto);
            return "redirect:/main/main";
        }
        return "redirect:/auth/login?error=true";
    }
}
