package com.maximys.diary.scheduler;

import com.maximys.diary.controller.MainController;
import com.maximys.diary.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class TokenCleanupScheduler {

    @Autowired
    private TokenService tokenService;

    Logger logger = LoggerFactory.getLogger(MainController.class);

    @Scheduled(fixedRate = 86400000) // 86400000 миллисекунд = 24 часа
    public void cleanupTokens() {
        tokenService.deleteOldTokens();
        logger.info("Выполнена плановая очистка старых токенов");
    }
}