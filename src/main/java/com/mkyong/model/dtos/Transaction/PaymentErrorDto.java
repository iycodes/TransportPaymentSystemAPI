package com.mkyong.model.dtos.Transaction;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PaymentErrorDto {
    private String error_msg;
    private int sender_id;
    private int receiver_id;
    private BigDecimal amount;

}
