package com.mkyong.model.dtos;

import lombok.Data;

@Data
public class LogoutDto {
    String sessionId;
    String userId;
}
