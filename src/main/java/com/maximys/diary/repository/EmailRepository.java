package com.maximys.diary.repository;

import com.maximys.diary.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailRepository extends JpaRepository<Email, String> {
    Email findByAddress(String address);
    boolean existsByAddress(String address);
}
