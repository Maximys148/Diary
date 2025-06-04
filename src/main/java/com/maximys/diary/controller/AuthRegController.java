package com.maximys.diary.controller;

import com.maximys.diary.dto.JwtAuthenticationResponse;
import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.dto.RegistrationDTO;
import com.maximys.diary.service.AuthService;
import com.maximys.diary.service.JwtService;
import com.maximys.diary.service.UserDetailsServiceImpl;
import com.maximys.diary.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/start")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthRegController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final Logger log = LogManager.getLogger(AuthRegController.class);


    @Operation(summary = "Стартовая страница")
    @GetMapping("/welcome")
    public ModelAndView welcomePage() {  // Убрали @CookieValue и проверку токена
        return new ModelAndView("welcome"); // Всегда возвращаем страницу welcome
    }

    @Operation(summary = "Страница авторизации")
    @GetMapping("/login")
    public ModelAndView showLoginPage(
            @CookieValue(value = "jwtToken", required = false) String token,
            HttpServletRequest request) {

        // Проверяем, не пришли ли мы уже с /login (чтобы избежать цикла)
        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("/login")) {
            return new ModelAndView("login");
        }

        if (token != null && jwtTokenUtil.validateToken(token)) {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            if (username != null) {
                return new ModelAndView("redirect:/main/main");
            }
        }

        return new ModelAndView("login");
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public ModelAndView signIn(
            @ModelAttribute @Valid LoginDTO loginDTO,
            HttpServletResponse response) {

        JwtAuthenticationResponse authResponse = authService.signIn(loginDTO);

        // Устанавливаем JWT в cookie
        ResponseCookie cookie = buildJwtCookie(authResponse.getToken());
        response.addHeader("Set-Cookie", cookie.toString());

        return new ModelAndView("redirect:/main/main");
    }

    @Operation(summary = "Выход из системы")
    @GetMapping("/logout")
    public ModelAndView logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwtToken", "")
                .maxAge(0)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return new ModelAndView("redirect:/start/login");
    }


    // Метод для создания JWT cookie
    private ResponseCookie buildJwtCookie(String token) {
        return ResponseCookie.from("jwtToken", token)
                .httpOnly(true)
                .secure(false) // В production должно быть true
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 дней
                .sameSite("Lax")
                .build();
    }

    // Остальные методы (регистрация и валидация) остаются без изменений
    @Operation(summary = "Страница регистрации")
    @GetMapping("/register")
    public ModelAndView showRegistrationPage() {
        return new ModelAndView("registration");
    }

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    public ModelAndView signUp(@ModelAttribute @Valid RegistrationDTO registrationDTO) {
        JwtAuthenticationResponse response = authService.signUp(registrationDTO);
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("token", response.getToken());
        modelAndView.addObject("username", registrationDTO.getUserName());
        return modelAndView;
    }

    @Operation(summary = "Валидация токена")
    @GetMapping("/validate")
    public ModelAndView validateToken(@RequestParam String token) {
        String username = jwtService.extractUserName(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        boolean isValid = jwtService.isTokenValid(token);
        ModelAndView modelAndView = new ModelAndView("validate");
        modelAndView.addObject("isValid", isValid);
        modelAndView.addObject("username", username);
        return modelAndView;
    }
}