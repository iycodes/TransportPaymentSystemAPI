package com.mkyong.responses;

import org.springframework.http.ResponseEntity;

import lombok.Data;

@Data
public class VerifyEmailResponse {
    String message;
    ResponseEntity responseEntity;

    public VerifyEmailResponse() {
    }
}
