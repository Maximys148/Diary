package com.maximys.diary.service;

import com.maximys.diary.entity.Token;
import com.maximys.diary.entity.User;
import com.maximys.diary.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    private TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token createToken(User user) {
        Token token = new Token();
        token.setUser(user);
        token.setExpirationTime(LocalDateTime.now().plusHours(1)); // Установите время жизни токена
        String newId = LocalDateTime.now() + "." + System.currentTimeMillis() + "." + (Math.random() * Integer.MAX_VALUE);
        UUID uuid = UUID.nameUUIDFromBytes(newId.getBytes(StandardCharsets.UTF_8));
        token.setName(uuid.toString().toUpperCase()); // Установите имя токена, если это необходимо
        return tokenRepository.save(token);
    }

    public boolean validateToken(Token token) {
        if(tokenRepository.findByName(token.getName()).isPresent()){
            if(LocalDateTime.now().isAfter(token.getExpirationTime())){
                //  Срок токена вышел, удаляем из бд его
                tokenRepository.delete(token);
                return false;
            }
            return true;
        }
        return false;
    }

    public void refreshToken(Token token1) {
        tokenRepository.findByName(token1.getName()).ifPresent(token -> {
            token.setExpirationTime(LocalDateTime.now().plusMinutes(5));
            tokenRepository.save(token);
        });
    }

    public Optional<User> getUserByToken(Token token){
        return tokenRepository.findUserByToken(token.getName());
    }
}
