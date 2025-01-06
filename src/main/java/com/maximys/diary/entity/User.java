package com.maximys.diary.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(generator = "id_user", strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Email> emails;

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
    }
    // Метод для хеширования пароля
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] byteData = md.digest();

            // Преобразуем байты в шестнадцатеричную строку
            StringBuilder sb = new StringBuilder();
            for (byte b : byteData) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Не удалось хешировать пароль", e);
        }
    }
}
