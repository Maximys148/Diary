package com.maximys.diary.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

/**
 * Универсальный компонент для работы с JWT: генерация, валидация, извлечение данных,
 * а также работа с HTTP-куками.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long lifetime; // В миллисекундах

    private static final String JWT_COOKIE_NAME = "jwtToken";

    /**
     * Генерирует JWT по данным Authentication.
     */
    public String generateToken(Authentication authentication) {
        validateAuthentication(authentication);
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return buildToken(userPrincipal.getUsername(), lifetime);
    }

    public String generateToken(UserDetails userDetails) {
        if (userDetails == null || userDetails.getUsername() == null || userDetails.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Invalid UserDetails");
        }
        return buildToken(userDetails.getUsername(), lifetime);
    }

    /**
     * Генерирует refresh-токен по данным Authentication.
     */
    public String generateRefreshToken(Authentication authentication) {
        validateAuthentication(authentication);
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return buildToken(userPrincipal.getUsername(), lifetime * 2);
    }

    /**
     * Генерирует JWT по username.
     */
    public String generateTokenByUsername(String username) {
        return buildToken(username, lifetime);
    }

    /**
     * Внутренний метод для построения токена.
     */
    private String buildToken(String username, long expirationMillis) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Валидирует токен.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Можно логировать ошибку
            return false;
        }
    }

    /**
     * Проверяет, истёк ли токен.
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true; // недействительный или истекший токен
        }
    }

    /**
     * Извлекает username из токена.
     */
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Получает Claims из токена.
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Получает секретный ключ для подписи.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Проверяет корректность объекта Authentication.
     */
    private void validateAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Invalid authentication");
        }
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        if (userPrincipal.getUsername() == null || userPrincipal.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Invalid username");
        }
    }

    // Работа с куками

    /**
     * Извлекает JWT из HTTP-запроса по кукам.
     */
    public Optional<String> extractJwtFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        for (Cookie cookie : request.getCookies()) {
            if (JWT_COOKIE_NAME.equals(cookie.getName())) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }

    /**
     * Создает ResponseCookie для хранения JWT.
     */
    public ResponseCookie buildJwtCookie(String token) {
        return buildCookie(JWT_COOKIE_NAME, token, lifetime / 1000);
    }

    /**
     * Создает ResponseCookie для refresh-токена.
     */
    public ResponseCookie buildRefreshCookie(String token) {
        return buildCookie("refreshToken", token, lifetime / 1000);
    }

    /**
     * Создает ResponseCookie для выхода — удаляет cookie.
     */
    public ResponseCookie buildLogoutCookie() {
        return buildCookie(JWT_COOKIE_NAME, "", 0);
    }

    /**
     * Вспомогательный метод для построения cookie.
     */
    private ResponseCookie buildCookie(String name, String value, long maxAgeSeconds) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                // В production рекомендуется установить secure(true)
                .secure(false)
                .path("/")
                .maxAge(maxAgeSeconds)
                .sameSite("Lax")
                .build();
    }
}
