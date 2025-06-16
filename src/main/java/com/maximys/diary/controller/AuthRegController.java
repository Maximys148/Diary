package com.maximys.diary.controller;

import com.maximys.diary.dto.JwtAuthenticationResponse;
import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.dto.RegistrationDTO;
import com.maximys.diary.service.AuthService;
import com.maximys.diary.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/start")
@Tag(name = "Аутентификация")
public class AuthRegController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final Logger log = LogManager.getLogger(AuthRegController.class);


    private static final String ERROR_VIEW_PREFIX = "error/"; // Префикс для error-страниц

    @Autowired
    public AuthRegController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    /* Стартовая страница */
    @Operation(summary = "Стартовая страница")
    @GetMapping("/welcome")
    public ModelAndView welcomePage() {
        log.info("[GET] /start/welcome - Отображение стартовой страницы");
        return new ModelAndView("welcome");
    }

    /* Страница авторизации */
    @Operation(summary = "Страница авторизации")
    @GetMapping("/login")
    public ModelAndView showLoginPage(
            @CookieValue(value = "jwtToken", required = false) String token,
            HttpServletRequest request) {

        log.debug("[GET] /start/login - Попытка входа. Проверка реферера и токена");

        try {
            String referer = request.getHeader("Referer");
            if (referer != null && referer.contains("/login")) {
                log.warn("Обнаружен циклический редирект с /login");
                return new ModelAndView("login");
            }

            if (token != null && jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                if (username != null) {
                    log.info("Пользователь {} уже аутентифицирован. Редирект на /main", username);
                    return new ModelAndView("redirect:/main/main");
                }
            }

            log.info("Отображение страницы входа для нового пользователя");
            return new ModelAndView("login");

        } catch (Exception e) {
            log.error("Ошибка при отображении страницы входа: {}", e.getMessage());
            return handleError(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* Обработка авторизации */
    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    public ModelAndView signIn(
            @ModelAttribute @Valid LoginDTO loginDTO,
            HttpServletResponse response) {

        log.info("[POST] /start/login - Попытка входа для пользователя {}", loginDTO.getUserName());

        try {
            JwtAuthenticationResponse authResponse = authService.signIn(loginDTO);

            // Устанавливаем куку
            ResponseCookie cookie = ResponseCookie.from("jwtToken", authResponse.getToken())
                    .httpOnly(true)
                    .secure(false) // true в production
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // 7 дней
                    .sameSite("Lax")
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());
            log.debug("Установлена JWT кука для пользователя: {}", loginDTO.getUserName());
            return new ModelAndView("redirect:/main/main");

        } catch (AuthenticationException e) {
            log.warn("Ошибка аутентификации: {}", e.getMessage());
            ModelAndView model = new ModelAndView("login");
            model.addObject("error", "Неверные учетные данные");
            return model;
        } catch (Exception e) {
            log.error("Системная ошибка при авторизации: {}", e.getMessage());
            return handleError(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* Выход из системы */
    @Operation(summary = "Выход из системы")
    @GetMapping("/logout")
    public ModelAndView logout(HttpServletResponse response) {
        log.info("[GET] /start/logout - Выход пользователя из системы");

        try {
            ResponseCookie cookie = ResponseCookie.from("jwtToken", "")
                    .maxAge(0)
                    .path("/")
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());
            return new ModelAndView("redirect:/start/login");
        } catch (Exception e) {
            log.error("Ошибка при выходе из системы: {}", e.getMessage());
            return handleError(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* Страница регистрации */
    @Operation(summary = "Страница регистрации")
    @GetMapping("/register")
    public ModelAndView showRegistrationPage() {
        log.info("[GET] /start/register - Отображение страницы регистрации");
        return new ModelAndView("register"); // название шаблона страницы регистрации
    }

    /* Обработка формы регистрации */
    @Operation(summary = "Регистрация нового пользователя")
    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute @Valid RegistrationDTO registerDTO, HttpServletResponse response) {
        log.info("[POST] /start/register - Попытка зарегистрировать пользователя {}", registerDTO.getUserName());
        try {
            authService.signUp(registerDTO); // ваш сервис должен реализовать регистрацию
            // Удаляем старую куку с токеном
            ResponseCookie cookie = ResponseCookie.from("jwtToken", "")
                    .maxAge(0)
                    .path("/")
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());
            log.info("Пользователь {} успешно зарегистрирован", registerDTO.getUserName());
            return new ModelAndView("redirect:/start/login"); // перенаправление на страницу входа
        } catch (Exception e) {
            log.error("Ошибка при регистрации: {}", e.getMessage());
            ModelAndView model = new ModelAndView("register");
            model.addObject("error", "Ошибка при регистрации: " + e.getMessage());
            return model;
        }
    }
    // ... остальные методы с аналогичным логированием ...

    /* Обработка ошибок */
    private ModelAndView handleError(HttpStatus status) {
        String errorView = ERROR_VIEW_PREFIX + status.value();
        log.error("Отображение страницы ошибки: {}", errorView);

        ModelAndView model = new ModelAndView(errorView);
        model.addObject("status", status.value());
        model.addObject("error", status.getReasonPhrase());
        return model;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex) {
        log.error("Необработанное исключение: {}", ex.getMessage());
        return handleError(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}