package com.maximys.diary.controller;

import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.dto.RegistrationDTO;
import com.maximys.diary.service.AuthService;
import com.maximys.diary.service.TokenService;
import com.maximys.diary.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private TokenService tokenService;
    private UserService userService;
    Logger logger = LoggerFactory.getLogger(MainController.class);

    public AuthRegController(AuthService authService, TokenService tokenService, UserService userService) {
        this.authService = authService;
        this.tokenService = tokenService;
        this.userService = userService;
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
            logger.info(registrationDto.getNickName() + ", успешно зарегистрировался");
            return "redirect:/start/login";
        }
        logger.error(registrationDto.getNickName() + ", не смог зарегистрироваться");
        return"redirect:/start/register";
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
            session.setAttribute("token", tokenService.createToken(userService.getUser(loginDto)));
            logger.info(loginDto.getNickName() + ", успешно авторизовался");
            return "redirect:/main/main";
        }
        logger.error(loginDto.getNickName() + ", не смог авторизовался");
        return "redirect:/start/register";
    }
}
