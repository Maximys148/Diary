package com.maximys.diary.filter;

import com.maximys.diary.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;

@Slf4j
@Component
public class JwtCookieAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String requestUri = request.getRequestURI();

        // Пропускаем проверку для публичных эндпоинтов
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Optional<String> optionalJWT = jwtUtil.extractJwtFromRequest(request);
            log.debug("JWT из куков: {}", optionalJWT);

            boolean shouldRefreshToken = false;
            String username = null;

            if (optionalJWT.isPresent()) {
                String jwt = optionalJWT.get();
                username = jwtUtil.getUsernameFromToken(jwt);
                try {
                    if (jwtUtil.validateToken(jwt)) {
                        authenticateUser(username, request);
                    }
                } catch (ExpiredJwtException ex) {
                    log.warn("Токен просрочен, пытаемся обновить для пользователя: {}", ex.getClaims().getSubject());
                    username = ex.getClaims().getSubject();
                    shouldRefreshToken = true;
                } catch (JwtException ex) {
                    log.warn("Невалидный токен: {}", ex.getMessage());
                    // Пытаемся извлечь username для возможного обновления
                    shouldRefreshToken = username != null;
                }
            }

            // Если требуется обновление токена и username известен
            if (shouldRefreshToken && username != null) {
                if (refreshTokenForUser(username, request, response)) {
                    // После успешного обновления продолжаем цепочку
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            // Если аутентификация не прошла
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                handleUnauthorized(response, "Требуется аутентификация");
                return;
            }

        } catch (Exception e) {
            log.error("Ошибка аутентификации: {}", e.getMessage());
            handleUnauthorized(response, "Ошибка аутентификации");
            return;
        }

        filterChain.doFilter(request, response);
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
                path.startsWith("/swagger-ui");
    }
}