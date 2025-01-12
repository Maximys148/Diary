package com.maximys.diary.service;

import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.entity.Email;
import com.maximys.diary.entity.User;
import com.maximys.diary.repository.EmailRepository;
import com.maximys.diary.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private EmailRepository emailRepository;

    public UserService(UserRepository userRepository, EmailRepository emailRepository) {
        this.userRepository = userRepository;
        this.emailRepository = emailRepository;
    }

    public User getUser(LoginDTO dto){
        if(userRepository.findByNickName(dto.getNickName()) != null){
            User byNickName = userRepository.findByNickName(dto.getNickName());
            return byNickName;
        }
        return null;
    }
    public boolean createAndLinkEmailToUser(String nickName, String emailAddress) {
        // Поиск пользователя по нику
        User user = userRepository.findByNickName(nickName);
        if (user != null) {
            // Создание нового объекта Email
            Email newEmail = new Email(emailAddress, user); // Здесь мы подразумеваем, что User провязан в конструкторе

            // Сохраняем Email в базу данных
            emailRepository.save(newEmail);

            // Обновляем список почт пользователя, если требуется (можно создать метод в User)
            List<Email> emails = user.getEmails();
            emails.add(newEmail);
            user.setEmails(emails);

            // Сохраняем обновленного пользователя
            userRepository.save(user);

            return true; // Успешно создали и связали почту
        } else {
            return false; // Пользователь не найден
        }
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
