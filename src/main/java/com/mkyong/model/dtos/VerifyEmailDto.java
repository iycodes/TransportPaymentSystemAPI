package com.mkyong.model.dtos;

import lombok.Data;

@Data
public class VerifyEmailDto {
    private String recepientEmail;
    private String name;
    private int code;

}
