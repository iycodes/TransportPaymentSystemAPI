package com.mkyong.responses;

import org.springframework.http.HttpStatus;

import com.mkyong.model.enums.TxStatus;

import lombok.Data;

@Data
public class NewTxResponse {
    private String msg;
    private String error;
    private HttpStatus statusCode;
    private TxStatus txStatus;

    public NewTxResponse(String err, String msg, HttpStatus code, TxStatus status_) {
        error = err;
        statusCode = code;
        txStatus = status_;
    }

    public NewTxResponse(String msg_) {
        msg = msg_;
        statusCode = HttpStatus.OK;
        txStatus = TxStatus.success;
    }

    static public NewTxResponse pending() {
        return new NewTxResponse(null, "account funding is pending", HttpStatus.ACCEPTED, TxStatus.pending);
    }

    static public NewTxResponse success() {
        return new NewTxResponse(null, "account funding is succesfull", HttpStatus.OK, TxStatus.success);
    }

    static public NewTxResponse error(String errorMessage, HttpStatus status_) {
        return new NewTxResponse(errorMessage, null, status_, TxStatus.failed);
    }
}
