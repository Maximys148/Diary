package com.maximys.diary.service;

import com.maximys.diary.dto.JwtAuthenticationResponse;
import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.dto.RegistrationDTO;
import com.maximys.diary.entity.User;
import com.maximys.diary.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.maximys.diary.enums.Role.ROLE_USER;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    public JwtAuthenticationResponse signUp(RegistrationDTO registrationDTO) {
        // Проверка, что пользователь с таким именем не существует
        if (userService.existsByUserName(registrationDTO.getUserName())) {
            throw new RuntimeException("Username already exists");
        }

        var user = User.builder()
                .userName(registrationDTO.getUserName())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .firstName(registrationDTO.getFirstName())
                .lastName(registrationDTO.getLastName())
                .middleName(registrationDTO.getMiddleName())
                .role(ROLE_USER)
                .build();

        userService.create(user);

        var jwt = jwtUtil.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(LoginDTO loginDTO) {
        try {
            // Аутентификация через AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUserName(),
                            loginDTO.getPassword()
                    )
            );

            // Загрузка пользователя
            var user = userService
                    .userDetailsService()
                    .loadUserByUsername(loginDTO.getUserName());

            // Генерация токена
            var jwt = jwtUtil.generateToken(user);
            return new JwtAuthenticationResponse(jwt);

        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password", e);
        }
    }
}