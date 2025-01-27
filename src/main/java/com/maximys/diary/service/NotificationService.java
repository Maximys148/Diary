package com.maximys.diary.service;

import com.maximys.diary.dto.EventDTO;
import com.maximys.diary.entity.Diary;
import com.maximys.diary.entity.Notification;
import com.maximys.diary.entity.User;
import com.maximys.diary.enums.UnitTime;
import com.maximys.diary.repository.DiaryRepository;
import com.maximys.diary.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
                notification.setAlertTime(alertTime);
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

    public List<Notification> getUpcomingNotifications(User user) {
        List<Notification> notifications = findByUser(user);
        LocalDateTime now = LocalDateTime.now();
        return notifications.stream()
                .filter(notification -> notification.getAlertTime().isAfter(now))
                .collect(Collectors.toList());
    }

    public HashMap<Notification, String> getTimeDifferenceNotification(List<Notification> notifications) {
        HashMap<Notification, String> timeDifferences = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();

        for (Notification notification : notifications) {
            LocalDateTime alertTime = notification.getAlertTime();

            // Вычисляем разницу во времени
            Duration duration = Duration.between(now, alertTime);
            long seconds = duration.getSeconds();

            // Проверка, осталось ли время до уведомления
            if (seconds < 0) {
                // Если время уже прошло, можем указать, что событие прошло
                timeDifferences.put(notification, "Событие прошло");
            } else {
                long hours = seconds / 3600;
                long minutes = (seconds % 3600) / 60;
                long remainingSeconds = seconds % 60;

                // Формируем строку с оставшимся временем
                String remainingTime = String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
                timeDifferences.put(notification, remainingTime);
            }
        }

        return timeDifferences;
    }
}