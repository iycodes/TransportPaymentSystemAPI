package com.mkyong.model.dtos.Transaction;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;

import com.beust.jcommander.internal.Nullable;
import com.mkyong.model.enums.TxStatus;

import lombok.Data;

@Data
public class PaymentResponseDto {

    private String txId;
    // private BigDecimal amount;
    // private int senderId;
    // private int reveiverId;
    private TxStatus txStatus;
    private String errorMsg;
    private HttpStatus errorCode;

    public PaymentResponseDto(String txId_, TxStatus txStatus_) {
        this.txId = txId_;
        this.txStatus = txStatus_;
    }

    public PaymentResponseDto(String txId_, TxStatus txStatus_, String errorMSg_, HttpStatus errorCode_) {
        this.txId = txId_;
        this.txStatus = txStatus_;
        this.errorMsg = errorMSg_;
        this.errorCode = errorCode_;
    }

    public PaymentResponseDto(String txId_, TxStatus txStatus_, String errorMSg_) {
        this.txId = txId_;
        this.txStatus = txStatus_;
        this.errorMsg = errorMSg_;
    }

}
