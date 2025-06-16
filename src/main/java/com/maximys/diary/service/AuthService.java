package com.maximys.diary.service;

import com.maximys.diary.dto.JwtAuthenticationResponse;
import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.dto.RegistrationDTO;
import com.maximys.diary.entity.User;
import com.maximys.diary.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.maximys.diary.enums.Role.ROLE_USER;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

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
        // 1. Находим пользователя в БД
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(loginDTO.getUserName());

        // 2. Проверяем пароль
        if (!passwordEncoder.matches(loginDTO.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Неверный пароль");
        }

        // 3. Генерируем JWT
        String token = jwtUtil.generateToken(userDetails);

        return new JwtAuthenticationResponse(token);
    }
}