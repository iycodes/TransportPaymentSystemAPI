package com.mkyong.model.dtos;

import lombok.Data;

@Data
public class LoginUserDto {
    private String email;
    private String password;
    private String fcmToken;

    public LoginUserDto(String email_, String password_, String fcmToken_) {
        email = email_;
        password = password_;
        fcmToken = fcmToken_;
    }

}
