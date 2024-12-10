package com.mkyong.responses;

import org.springframework.http.HttpStatus;

import com.mkyong.model.enums.TxStatus;

import lombok.Data;

@Data
public class FundAccountResponse {
    private String msg;
    private String error;
    private HttpStatus statusCode;
    private TxStatus txStatus;

    public FundAccountResponse(String err, String msg, HttpStatus code, TxStatus status_) {
        error = err;
        statusCode = code;
        txStatus = status_;
    }

    public FundAccountResponse(String msg_) {
        msg = msg_;
        statusCode = HttpStatus.OK;
        txStatus = TxStatus.success;
    }

    static public FundAccountResponse pending() {
        return new FundAccountResponse(null, "account funding is pending", HttpStatus.ACCEPTED, TxStatus.pending);
    }

    static public FundAccountResponse success() {
        return new FundAccountResponse(null, "account funding is succesfull", HttpStatus.OK, TxStatus.success);
    }

    static public FundAccountResponse error(String errorMessage, HttpStatus status_) {
        return new FundAccountResponse(errorMessage, null, status_, TxStatus.failed);
    }
}
