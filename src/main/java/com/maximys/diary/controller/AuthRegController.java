package com.maximys.diary.controller;

import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.dto.RegistrationDTO;
import com.maximys.diary.service.AuthService;
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
      //  authService.register(registrationDto);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDto", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(LoginDTO loginDto) {
      //  if (authService.login(loginDto)) {
       //     return "redirect:/home";
       // }
        return "redirect:/auth/login?error=true";
    }
}
