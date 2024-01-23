package com.mkyong.model.dtos;

import com.mkyong.model.Userr;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoginUserDto {
    private String email;
    private String password;
}
