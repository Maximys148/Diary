package com.maximys.diary.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maximys.diary.dto.RegistrationDTO;
import jakarta.persistence.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(generator = "user_id", strategy = GenerationType.AUTO)
    private Long id;
    private String nickName;
    private String firstName;
    private String lastName;
    private String middleName;
    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Email> emails;

    public User(String nickName, String firstName, String lastName, String middleName, String password, List<Email> emails) {
        this.nickName = nickName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.password = password;
        this.emails = emails;
    }
    public User(RegistrationDTO dto){
        this.nickName = dto.getNickName();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.middleName = dto.getMiddleName();
        this.password = dto.getPassword();
    }

    public User() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

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
        this.password = password;
    }
}
