package com.maximys.diary.controller;

import com.maximys.diary.entity.User;
import com.maximys.diary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthRegController {
    @Autowired
    private UserService userService;

    public AuthRegController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration"; // Шаблон для страницы регистрации
    }

    /*@PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        // Проверка на наличие пользователя и сохранение его в базе данных
        if (userService.validateUser(user.getPassword(), user.getEmail())) {
            model.addAttribute("message", "Пароль или почта указаны неверно, либо они заняты другим пользователем");
            return "registration"; // Возвращаем на страницу регистрации
        }

        userService.saveUser(user); // Сохранение пользователя
        return "redirect:/home"; // Перенаправление на главную страницу
    }*/
}
