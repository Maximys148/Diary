package com.maximys.diary.error;

import com.maximys.diary.error.dto.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtAuthenticationErrorHandler {
    private final Logger log = LogManager.getLogger(JwtAuthenticationErrorHandler.class);

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredToken(ExpiredJwtException ex) {
        log.warn("Токен просрочен: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("TOKEN_EXPIRED", "Токен просрочен"));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(JwtException ex) {
        log.warn("Невалидный токен: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("INVALID_TOKEN", "Недействительный токен"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Ошибка аутентификации: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("AUTH_FAILED", "Ошибка аутентификации"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Ошибка сервера: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("SERVER_ERROR", "Внутренняя ошибка сервера"));
    }
}