package com.mkyong.model.dtos;

import com.mkyong.model.UserEntity;
import lombok.Data;
import org.apache.catalina.User;

import java.math.BigDecimal;

@Data
public class RegisterUserDto {
    private String email;
    private String password;
    private String name;
    private String phoneNo;
    private int verificationCode;
    private String fcmToken;

    public UserEntity toUserr() {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setPhoneNo(phoneNo);
        // user.setFcm
        return user;
    }

}
