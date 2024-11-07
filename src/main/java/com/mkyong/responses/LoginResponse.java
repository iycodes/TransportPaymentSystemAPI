package com.mkyong.responses;

import com.mkyong.model.UserEntity;
import com.mkyong.model.dtos.AuthDataDto;

import lombok.Data;

@Data
public class LoginResponse {

    private Long expiresIn;
    private UserEntity userEntity;
    private String errorMsg;
    public AuthDataDto authDataDto;

    public LoginResponse(Long expiresIn_, AuthDataDto authDataDto_, UserEntity userEntity_) {
        this.expiresIn = expiresIn_;
        this.userEntity = userEntity_;
        this.authDataDto = authDataDto_;
    }

    public LoginResponse(String errorMsg_) {
        this.errorMsg = errorMsg_;
    }

}
