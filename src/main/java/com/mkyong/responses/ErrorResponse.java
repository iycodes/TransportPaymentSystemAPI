package com.mkyong.responses;

import lombok.Data;

@Data
public class ErrorResponse {
    Exception exception;
    String msg;

    public ErrorResponse(Exception exception_, String msg_) {
        this.exception = exception_;
        this.msg = msg_;
    }

    public ErrorResponse(String msg_) {
        this.msg = msg_;
    }

}
