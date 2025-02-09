package com.maximys.diary.dto;

import static com.maximys.diary.util.Password.hashPassword;

public class LoginDTO {
    private String nickName;
    private String password;

    public LoginDTO(String nickName, String password) {
        this.nickName = nickName;
        this.password = password;
    }

    public LoginDTO() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return hashPassword(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
