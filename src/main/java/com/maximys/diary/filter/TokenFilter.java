package com.maximys.diary.filter;

import com.maximys.diary.entity.Token;
import com.maximys.diary.service.TokenService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class TokenFilter implements Filter {

    @Autowired
    private TokenService tokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        // Проверяем наличие токена в сессии
        Token tokenValue = (Token) httpRequest.getSession().getAttribute("token");

        // Пропускаем запросы к /start и /start/login
        if (requestURI.equals("/start") || requestURI.equals("/start/login")) {
            chain.doFilter(request, response);
            return;
        }

        if (tokenValue != null) {
            if (!tokenService.validateToken(tokenValue)) {
                httpResponse.sendRedirect("/start/login");
                return;
            }
        }else {
            httpResponse.sendRedirect("/start/login");
            return;
        }
        tokenService.refreshToken(tokenValue);
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Инициализация фильтра, если необходимо
    }

    @Override
    public void destroy() {
        // Освобождение ресурсов, если необходимо
    }
}