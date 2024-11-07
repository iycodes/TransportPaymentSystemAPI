package com.mkyong.responses;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class FundAccountResponse {
    private String msg;
    private String error;
    private HttpStatus statusCode;

    public FundAccountResponse(String err, HttpStatus code) {
        error = err;
        statusCode = code;
    }

    public FundAccountResponse(String msg_) {
        msg = msg_;
        statusCode = HttpStatus.OK;
    }
}
