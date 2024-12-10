package com.mkyong.model.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FundAccountDto {
    private String userId;
    private BigDecimal amount;
    private String title;
    private String txRef;

    public FundAccountDto(String userId_, String title_, BigDecimal amount_, String txRef_) {
        userId = userId_;
        title = title_;
        amount = amount_;
        txRef = txRef_;
    }

}
