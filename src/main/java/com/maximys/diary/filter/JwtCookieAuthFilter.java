package com.maximys.diary.filter;

import com.maximys.diary.controller.AuthRegController;
import com.maximys.diary.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

@Component
public class JwtCookieAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    private final Logger log = LogManager.getLogger(JwtCookieAuthFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws  IOException {
        try {

            // Проверка нужно ли токен для этого url пути
            if (shouldNotFilter(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Проверка на наличие токена в куках
            Optional<String> optionalJWT = jwtUtil.extractJwtFromRequest(request);
            log.debug("JWT из куков: {}", optionalJWT);
            if (optionalJWT.isEmpty()) {
                handleUnauthorized(response, "Токен отсутствует");
                return;
            }

            // Проверка токена на валидность
            String jwt = optionalJWT.get();
            if (jwtUtil.validateToken(jwt)) {
                String username = jwtUtil.getUsernameFromToken(jwt);
                authenticateUser(username, request);
                filterChain.doFilter(request, response);
            }
        } catch (ExpiredJwtException ex) {
            // Токен просрочен, но claims читаемы
            String username = ex.getClaims().getSubject();
            log.warn("Токен просрочен для пользователя: {}", username);
            if (username != null) {
                String newToken = jwtUtil.generateTokenByUsername(username);
                ResponseCookie cookie = ResponseCookie.from("jwtToken", newToken)
                        .httpOnly(true)
                        .secure(true) // Для HTTPS!
                        .path("/")
                        .maxAge(Duration.ofDays(7).toSeconds())
                        .build();
                response.addHeader("Set-Cookie", cookie.toString());
                authenticateUser(username, request);
            }else {
                handleUnauthorized(response, "Не удалось извлечь данные пользователя");
            }
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Критическая ошибка аутентификации: {}", e.getMessage());
            handleUnauthorized(response, "Ошибка сервера");
        }
    }

    private boolean refreshTokenForUser(String username,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Создаем новую аутентификацию
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));

            // Генерируем новый токен
            String newToken = jwtUtil.generateRefreshToken(authentication);

            // Устанавливаем новую куку
            ResponseCookie newCookie = jwtUtil.buildJwtCookie(newToken);
            response.addHeader(HttpHeaders.SET_COOKIE, newCookie.toString());

            // Устанавливаем аутентификацию в контекст
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("Токен обновлен для пользователя: {}", username);
            return true;

        } catch (Exception e) {
            log.error("Ошибка при обновлении токена для {}: {}", username, e.getMessage());
            return false;
        }
    }

    private void authenticateUser(String username, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Аутентифицированный пользователь: {}", username);
    }

    private void handleTokenRefresh(String username, HttpServletResponse response) throws IOException {
        try {
            // 1. Проверяем, существует ли пользователь
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 2. Генерируем новый токен
            String newToken = jwtUtil.generateToken(
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    )
            );

            // 3. Устанавливаем новую куку
            ResponseCookie newCookie = jwtUtil.buildJwtCookie(newToken);
            response.addHeader(HttpHeaders.SET_COOKIE, newCookie.toString());

            // 4. Возвращаем успешный ответ
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Токен обновлен");

        } catch (Exception e) {
            log.error("Не удалось обновить токен: {}", e.getMessage());
            handleUnauthorized(response, "Не удалось обновить токен");
        }
    }

    private void handleUnauthorized(HttpServletResponse response, String message)
            throws IOException {
        SecurityContextHolder.clearContext();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/start") ||
                path.startsWith("/api/auth") ||
                path.startsWith("/error") ||
                path.startsWith("/favicon.ico") ||
                path.startsWith("/static") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui.html");
    }
}