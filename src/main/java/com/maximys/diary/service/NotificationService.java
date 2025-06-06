package com.maximys.diary.service;

import com.maximys.diary.dto.EventDTO;
import com.maximys.diary.entity.Notification;
import com.maximys.diary.entity.User;
import com.maximys.diary.enums.UnitTime;
import com.maximys.diary.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
// Сервис, отвечающий за уведомления
public class NotificationService {

    private final NotificationRepository notificationRepository; // Репозиторий для работы с дневником

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotifications(EventDTO eventDTO) {
        LocalDateTime eventTime = eventDTO.getDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(); // Преобразуем Date в LocalDateTime
        int interval = eventDTO.getLeadTime() * getSecond(eventDTO.getUnitTime()); // Интервал в секундах
        int frequency = eventDTO.getReminderFrequency(); // Количество уведомлений

        for (int i = 0; i < frequency; i++) {
            LocalDateTime alertTime = eventTime.minusSeconds((long) interval / (frequency - i));

            if (LocalDateTime.now().isBefore(alertTime)) {
                Notification notification = new Notification();
                notification.setEventName(eventDTO.getName());
                notification.setUser(eventDTO.getUser());
                notification.setRead(false);
                notification.setEventDateTime(alertTime);
                notificationRepository.save(notification);
            }
        }
    }

    private Integer getSecond(UnitTime unitTime) {
        switch (unitTime) {
            case DAY:
                return 60 * 60 * 24 ;
            case WEEK:
                return 60 * 60 * 24 * 7;
            case MONTH:
                return 60 * 60 * 24 * 7 * 30;
            case HOUR:
                return 60 * 60;
            case MINUTE:
                return 60;
            default:
                throw new IllegalArgumentException("Unsupported unit time: " + unitTime);
        }
    }

    public List<Notification> findByUser(User user){
        return notificationRepository.findByUser(user);
    }

    public List<Notification> getUpcomingNotifications(Long id) {
        if (id == null) {
            return Collections.emptyList();
        }
        // Ваша логика получения уведомлений
        List<Notification> notifications = notificationRepository.findByUserId(id);
        return notifications != null ? notifications : Collections.emptyList();
    }

    public HashMap<String, String> getTimeDifferenceNotification(List<Notification> notifications) {
        HashMap<String, String> timeDifferences = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        for (Notification notification : notifications) {
            LocalDateTime eventDateTime = notification.getEventDateTime();
            String eventName = notification.getEventName();
            if (eventDateTime == null) {
                // Обработка случая, когда eventDateTime не задан
                timeDifferences.put(eventName, "Нет времени уведомления");
                continue;
            }

            Duration duration = Duration.between(now, eventDateTime);
            long seconds = duration.getSeconds();

            if (seconds < 0) {
                timeDifferences.put(eventName, "Событие прошло");
            } else {
                long hours = seconds / 3600;
                long minutes = (seconds % 3600) / 60;
                long remainingSeconds = seconds % 60;

                String remainingTime = String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
                timeDifferences.put(eventName, remainingTime);
            }
        }

        return timeDifferences;
    }
}