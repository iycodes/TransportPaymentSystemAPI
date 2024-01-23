package com.mkyong.model.dtos;

import com.mkyong.model.Userr;
import lombok.Data;
import org.apache.catalina.User;

import java.math.BigDecimal;

@Data
public class RegisterUserDto {
    private String email;
    private String password;
    private String fullName;
    private String phoneNo;

}

