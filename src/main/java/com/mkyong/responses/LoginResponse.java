package com.mkyong.responses;


import lombok.Data;

@Data
public class LoginResponse {
    private String jwtToken;
    private Long expiresIn;
}
