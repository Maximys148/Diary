package com.maximys.diary.service;

import com.maximys.diary.entity.Diary;
import com.maximys.diary.entity.User;
import com.maximys.diary.enums.UnitTime;
import com.maximys.diary.repository.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
// Сервис, отвечающий за уведомления
public class NotificationService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final DiaryRepository diaryRepository; // Репозиторий для работы с дневником

    @Autowired
    public NotificationService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public void start() {
        // Запускаем процесс проверки событий каждую минуту
        scheduler.scheduleAtFixedRate(this::processNotifications, 0, 1, TimeUnit.MINUTES);
    }

    private void processNotifications() {
        List<Diary> diaries = diaryRepository.findAll(); // Получаем все записи из БД
        for (Diary diary : diaries) {
            scheduleNotification(diary);
        }
    }

    public void scheduleNotification(Diary diary) {
        long eventTime = diary.getDateTime().getTime(); // Запланированное время события
        long leadTimeMillis = diary.getLeadTime() * getUnitTimeMultiplier(diary.getUnitTime()); // Время напоминания в миллисекундах
        long reminderStartTime = eventTime - leadTimeMillis; // Время начала отправки уведомлений

        // Проверяем, можно ли запланировать напоминания
        if (reminderStartTime > System.currentTimeMillis()) {
            long timeUntilReminder = leadTimeMillis / diary.getReminderFrequency(); // Интервал между напоминаниями в миллисекундах
            for (int i = 0; i < diary.getReminderFrequency(); i++) {
                // Рассчитываем задержку для каждого уведомления
                long delay = reminderStartTime - System.currentTimeMillis() - (i * timeUntilReminder);

                /*if (delay > 0) {
                    scheduler.schedule(() -> sendNotification(diary), delay, TimeUnit.MILLISECONDS);
                }*/
            }
        } else {
            System.out.println("Дата события уже пропущена или напоминание невозможно запланировать.");
        }
    }

    private long getUnitTimeMultiplier(UnitTime unitTime) {
        switch (unitTime) {
            case DAY:
                return TimeUnit.DAYS.toMillis(1);
            case WEEK:
                return TimeUnit.DAYS.toMillis(7);
            case HOUR:
                return TimeUnit.HOURS.toMillis(1);
            case MINUTE:
                return TimeUnit.MINUTES.toMillis(1);
            default:
                return 0;
        }
    }

    /*private void sendNotification(Diary diary) {
        User user = diaryRepository.getUserById(diary.getUser().getId()); // Получаем пользователя по Id события
        if (user != null) {
            if (user.isOnline()) {
                // Логика отображения всплывающего уведомления
                showPopupNotification(diary, user);
            } else if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                // Логика отправки email
                sendEmailNotification(diary, user.getEmail());
            } else {
                System.out.println("Пользователь офлайн и у него нет email.");
            }
        }
    }*/

    private void showPopupNotification(Diary diary, User user) {
        // Логика отображения всплывающего уведомления
        System.out.println("Showing notification for diary entry: " + diary.getId() + " to user: " + user.getId());
    }

    private void sendEmailNotification(Diary diary, String email) {
        // Логика отправки email
        System.out.println("Sending email for diary entry: " + diary.getId() + " to email: " + email);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}