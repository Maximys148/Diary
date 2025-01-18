/*
package com.maximys.diary.controller;

import com.maximys.diary.entity.User;
import com.maximys.diary.repository.UserRepository;
import com.maximys.diary.service.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(@RequestHeader("Authorization") String nickname) {
        User user = userRepository.findByNickName(nickname); // Метод для поиска по никнейму
        if (user != null) {
            tokenService.refreshToken(user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
    }
}
*/

