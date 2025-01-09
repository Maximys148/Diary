package com.maximys.diary.service;

import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.entity.User;
import com.maximys.diary.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(LoginDTO dto){
        if(userRepository.findByNickName(dto.getNickName()) != null){
            User byNickName = userRepository.findByNickName(dto.getNickName());
            return byNickName;
        }
        return null;
    }

    /*public boolean validateUser(String password, String email){
        // Есть ли почта в БД
        if(userRepository.existsByEmail(email)){
            User user = userRepository.findByEmail(email);
            // Совпадает ли пароль
            if(user.getPassword().equals(password)){
                return true;
            }
            return false;
        }
        return false;
    }*/

    /*public boolean saveUser(User user){
        if(!userRepository.existsByEmail(user.getEmail())){
            userRepository.save(user);
            return true;
        }
        return false;
    }*/

}
