package com.mkyong.model.dtos;

import com.mkyong.model.UserEntity;

import lombok.Data;

@Data
public class AuthDataDto {
    private String userId;
    private String jwtAccessToken;
    private String refreshToken;
    private String sessionId;
    // private UserEntity userEntity;
}
