package com.maximys.diary.repository;

import com.maximys.diary.entity.Email;
import com.maximys.diary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
   // boolean existsByEmail(String email);
    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String username);
    boolean existsByEmail(Email email);
    // User findByEmail(String email);
}
