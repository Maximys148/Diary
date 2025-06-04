package com.maximys.diary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.maximys.diary.dto.EventDTO;
import com.maximys.diary.dto.MessageDTO;
import com.maximys.diary.entity.*;
import com.maximys.diary.enums.SendStatus;
import com.maximys.diary.service.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping(value = "/main")
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final DiaryService diaryService;
    private final UserService userService;
    private final EmailService emailService;
    private final MessageService messageService;
    private final NotificationService notificationService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public MainController(DiaryService diaryService,
                          UserService userService,
                          EmailService emailService,
                          MessageService messageService,
                          NotificationService notificationService,
                          JwtService jwtService,
                          ObjectMapper objectMapper) {
        this.diaryService = diaryService;
        this.userService = userService;
        this.emailService = emailService;
        this.messageService = messageService;
        this.notificationService = notificationService;
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            return userService.findByUserName(username);
        }
        throw new RuntimeException("User not authenticated");
    }


    @GetMapping(value = "/main")
    public ModelAndView showMainForm(ModelAndView model) throws JsonProcessingException {
        try {
            User user = getCurrentUser();
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            // Гарантируем не-null список
            List<Notification> notifications = Optional.ofNullable(notificationService.getUpcomingNotifications(user))
                    .orElse(Collections.emptyList());

            model.addObject("notifications", notifications);
            model.addObject("events", objectMapper.writeValueAsString(
                    diaryService.getEventsByUserId(user.getId())));
            model.addObject("currentTime", notificationService.getTimeDifferenceNotification(notifications));
            model.setViewName("main");
        } catch (Exception e) {
            logger.error("Error in showMainForm", e);
            model.addObject("notifications", Collections.emptyList());
            model.setViewName("main");
        }
        return model;
    }

    @GetMapping("/email")
    public ModelAndView emailPage(@RequestParam(required = false) String selectedEmail) {
        ModelAndView model = new ModelAndView("email");
        User user = getCurrentUser();
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
        } else {
            model.addObject("userEmail", null);
            model.addObject("messages", Collections.emptyList());
        }

        return model;
    }

    @PostMapping("/email")
    public String sendMessage(MessageDTO messageDTO) {
        User user = getCurrentUser();
        messageDTO.setSenderEmail(user.getEmail().getAddress());

        Message message = messageService.createMessage(messageDTO);
        message.setSendStatus(SendStatus.SENDING);
        messageService.updateMessage(message);

        boolean isSent = emailService.sendMessage(message);

        if (isSent) {
            message.setSendStatus(SendStatus.SEND);
            messageService.updateMessage(message);
            return "redirect:/main/email";
        }
        return "redirect:/main/email?error=send_failed";
    }

    @GetMapping("/profile")
    public ModelAndView viewProfile() {
        ModelAndView model = new ModelAndView("profile");
        model.addObject("user", getCurrentUser());
        return model;
    }

    @PostMapping("/profile")
    public String registerUser(String email) {
        User user = getCurrentUser();
        if (userService.createAndLinkEmailToUser(user.getUsername(), email)) {
            logger.info("{} успешно обновил профиль", user.getUsername());
            return "redirect:/main/profile";
        }
        logger.error("{} не смог обновить профиль", user.getUsername());
        return "redirect:/main/profile?error=email_exists";
    }

    @GetMapping("/event")
    public ModelAndView viewEvent() {
        ModelAndView model = new ModelAndView("event");
        model.addObject("events", diaryService.getEventsByUserId(getCurrentUser().getId()));
        return model;
    }

    @PostMapping("/event")
    public String addEvent(EventDTO eventDTO) {
        eventDTO.setUser(getCurrentUser());
        if (diaryService.addEvent(eventDTO)) {
            notificationService.createNotifications(eventDTO);
            return "redirect:/main/event";
        }
        return "redirect:/main/event?error=add_failed";
    }
}