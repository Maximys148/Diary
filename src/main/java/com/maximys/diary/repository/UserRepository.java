package com.maximys.diary.repository;

import com.maximys.diary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
   // boolean existsByEmail(String email);
    boolean existsByNickName(String nickName);
    User findByNickName(String nickName);
   // User findByEmail(String email);
}
