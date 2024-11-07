package com.mkyong.model.dtos;

import lombok.Data;

@Data
public class TokenDto {
    private String refreshToken;
    private String newJwtToken;
    private String message;

}
