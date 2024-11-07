package com.mkyong.model.dtos.Transaction;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PaymentSuccessDto {
    private String txId;
    private BigDecimal amount;
    private String userName;
    private String driverName;

}
