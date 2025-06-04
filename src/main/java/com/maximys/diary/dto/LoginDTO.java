package com.maximys.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static com.maximys.diary.util.Password.hashPassword;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Schema(description = "Запрос на аутентификацию")
public class LoginDTO {

    private String userName;

    private String password;

}
