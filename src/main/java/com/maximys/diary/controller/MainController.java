package com.maximys.diary.controller;

import com.maximys.diary.dto.EventDTO;
import com.maximys.diary.dto.MessageDTO;
import com.maximys.diary.entity.*;
import com.maximys.diary.enums.SendStatus;
import com.maximys.diary.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {
    private static final String ERROR_VIEW_PREFIX = "error/";
    private final DiaryService diaryService;
    private final UserService userService;
    private final EmailService emailService;
    private final MessageService messageService;
    private final NotificationService notificationService;
    private final Logger log = LogManager.getLogger(MainController.class);

    /* Главная страница */
    @GetMapping("/main")
    public ModelAndView showMainForm() {
        ModelAndView model = new ModelAndView("main");
        log.info("[GET] /main/main - Запрос главной страницы");

        try {
            Long id = userService.getUser().getId();

            List<Event> events = diaryService.getEventsByUserId(id);
            List<Notification> notifications = notificationService.getUpcomingNotifications(id);

            model.addObject("events", events);
            model.addObject("notifications", notifications);

            log.debug("Успешно загружено: {} уведомлений", notifications.size());

        } catch (Exception e) {
            log.error("Системная ошибка: {}", e.getMessage());
            return handleError(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return model;
    }

    /* Страница почты */
    @GetMapping("/email")
    public ModelAndView emailPage(@RequestParam(required = false) String selectedEmail) {
        log.info("[GET] /main/email - Запрос почты (selectedEmail={})", selectedEmail);
        ModelAndView model = new ModelAndView("email");

        try {
            User user = userService.getUser();
            Email email = user.getEmail();

            Map<String, Integer> unreadCounts = new HashMap<>();
            int unreadCount = messageService.getUnreadMessageCountForEmail(email.getAddress());
            unreadCounts.put(email.getAddress(), unreadCount);

            model.addObject("email", email);
            model.addObject("unreadCounts", unreadCounts);

            if (selectedEmail != null && !selectedEmail.isEmpty()) {
                List<Message> messages = messageService.getAllMessagesForEmail(selectedEmail);
                messageService.markMessagesAsRead(selectedEmail);
                model.addObject("userEmail", selectedEmail);
                model.addObject("messages", messages);
                log.debug("Загружено {} сообщений для {}", messages.size(), selectedEmail);
            }

        } catch (Exception e) {
            log.error("Ошибка загрузки почты: {}", e.getMessage());
            return handleError(HttpStatus.BAD_REQUEST);
        }

        return model;
    }

    /* Отправка сообщения */
    @PostMapping("/email")
    public ModelAndView sendMessage(MessageDTO messageDTO) {
        log.info("[POST] /main/email - Отправка сообщения на {}", messageDTO.getRecipientEmails());

        try {
            User user = userService.getUser();
            messageDTO.setSenderEmail(user.getEmail().getAddress());

            Message message = messageService.createMessage(messageDTO);
            message.setSendStatus(SendStatus.SENDING);
            messageService.updateMessage(message);

            if (emailService.sendMessage(message)) {
                message.setSendStatus(SendStatus.SEND);
                messageService.updateMessage(message);
                log.info("Сообщение успешно отправлено");
                return new ModelAndView("redirect:/main/email");
            } else {
                log.warn("Ошибка отправки сообщения");
                return handleError(HttpStatus.SERVICE_UNAVAILABLE);
            }

        } catch (Exception e) {
            log.error("Ошибка отправки: {}", e.getMessage());
            return handleError(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* Профиль пользователя */
    @GetMapping("/profile")
    public ModelAndView viewProfile() {
        log.info("[GET] /main/profile - Запрос профиля");
        ModelAndView model = new ModelAndView("profile");

        try {
            model.addObject("user", userService.getUser());
        } catch (Exception e) {
            log.error("Ошибка загрузки профиля: {}", e.getMessage());
            return handleError(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return model;
    }

    /* Обновление профиля */
    @PostMapping("/profile")
    public ModelAndView updateProfile(String email) {
        log.info("[POST] /main/profile - Обновление email на {}", email);

        try {
            User user = userService.getUser();
            if (userService.createAndLinkEmailToUser(user.getUsername(), email)) {
                log.info("Email успешно обновлён");
                return new ModelAndView("redirect:/main/profile");
            } else {
                log.warn("Email {} уже занят", email);
                ModelAndView model = new ModelAndView("profile");
                model.addObject("error", "email_exists");
                return model;
            }
        } catch (Exception e) {
            log.error("Ошибка обновления: {}", e.getMessage());
            return handleError(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* Страница событий */
    @GetMapping("/event")
    public ModelAndView viewEvent() {
        log.info("[GET] /main/event - Запрос списка событий");
        ModelAndView model = new ModelAndView("event");

        try {
            model.addObject("events",
                    diaryService.getEventsByUserId(userService.getUser().getId()));
        } catch (Exception e) {
            log.error("Ошибка загрузки событий: {}", e.getMessage());
            return handleError(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return model;
    }

    /* Добавление события */
    @PostMapping("/event")
    public ModelAndView addEvent(EventDTO eventDTO) {
        log.info("[POST] /main/event - Добавление события: {}", eventDTO.getName());

        try {
            eventDTO.setUser(userService.getUser());
            if (diaryService.addEvent(eventDTO)) {
                notificationService.createNotifications(eventDTO);
                log.debug("Событие создано с {} уведомлениями",
                        eventDTO.getReminderFrequency());
                return new ModelAndView("redirect:/main/main");
            } else {
                log.warn("Ошибка создания события");
                return handleError(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Ошибка: {}", e.getMessage());
            return handleError(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* Обработчик ошибок */
    private ModelAndView handleError(HttpStatus status) {
        String errorView = ERROR_VIEW_PREFIX + status.value();
        log.warn("Перенаправление на страницу ошибки: {}", errorView);

        ModelAndView model = new ModelAndView(errorView);
        model.addObject("status", status.value());
        model.addObject("error", status.getReasonPhrase());
        return model;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex) {
        log.error("Критическая ошибка: {}", ex.getMessage());
        return handleError(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}