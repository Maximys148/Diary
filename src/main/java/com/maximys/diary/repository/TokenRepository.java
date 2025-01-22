package com.maximys.diary.repository;

import com.maximys.diary.entity.Token;
import com.maximys.diary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    List<Token> findByUserId(Long userId);
    List<Token> findByCreatedAtBefore(LocalDateTime dateTime);
    Optional<Token> findByUser(User user);
    // Измените метод на что-то, что соответствует существующему полю
    Optional<Token> findByName(String token);
    @Query("SELECT t.user FROM Token t WHERE t.name = :tokenName")
    Optional<User> findUserByToken(@Param("tokenName") String tokenName);
}
