package com.maximys.diary.service;

import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.dto.RegistrationDTO;
import com.maximys.diary.entity.User;
import com.maximys.diary.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean saveUser(RegistrationDTO dto) {
        if(!userRepository.existsByNickName(dto.getNickName())){
            userRepository.save(new User(dto));
            return true;
        }
        return false;
    }

    public boolean login(LoginDTO dto){
        User userByNickname = userRepository.findByNickName(dto.getNickName());
        if(userByNickname != null){
            if(userByNickname.getPassword().equals(dto.getPassword())){
                return true;
            }
        }
        return false;
    }
}

