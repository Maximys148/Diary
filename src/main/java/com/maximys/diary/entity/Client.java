package com.maximys.diary.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GenericGenerator(name = "id_client", strategy = "com.maximys.diary.generator.StringIdGenerator")
    @GeneratedValue(generator = "id_client", strategy = GenerationType.AUTO)
    private String id;
    private String userName;
    private String email;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
