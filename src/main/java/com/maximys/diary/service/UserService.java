package com.maximys.diary.service;

import com.maximys.diary.dto.LoginDTO;
import com.maximys.diary.entity.Email;
import com.maximys.diary.entity.User;
import com.maximys.diary.repository.EmailRepository;
import com.maximys.diary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.maximys.diary.enums.Role.ROLE_ADMIN;
import static com.maximys.diary.enums.Role.ROLE_USER;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            return this.findByUserName(username);
        }
        throw new RuntimeException("Пользователь не аутентифицирован");
    }


    public User getUser(LoginDTO dto){
        if(userRepository.findByUserName(dto.getUserName()).isPresent()){
            return userRepository.findByUserName(dto.getUserName()).get();
        }
        return null;
    }
    public boolean createAndLinkEmailToUser(String nickName, String emailAddress) {
        // Поиск пользователя по нику
        Optional<User> optionalUser = userRepository.findByUserName(nickName);
        if(emailRepository.existsByAddress(emailAddress)){
            return false;
        }
        if (optionalUser.isPresent()) {
            User user1 = optionalUser.get();
            // Создание нового объекта Email
            Email newEmail = new Email(user1, emailAddress); // Здесь мы подразумеваем, что User провязан в конструкторе

            // Сохраняем Email в базу данных
            emailRepository.save(newEmail);

            // Обновляем список почт пользователя, если требуется (можно создать метод в User)
            user1.setEmail(user1.getEmail());

            // Сохраняем обновленного пользователя
            userRepository.save(user1);

            return true; // Успешно создали и связали почту
        } else {
            return false; // Пользователь не найден
        }
    }

    public User save(User user) {
        user.setRole(ROLE_USER);
        return userRepository.save(user);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public User create(User user) {
        if (userRepository.existsByUserName(user.getUsername())) {
            // Заменить на свои исключения
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        /*if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }*/


        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUsername(String username) {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Выдача прав администратора текущему пользователю
     * <p>
     * Нужен для демонстрации
     */

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
