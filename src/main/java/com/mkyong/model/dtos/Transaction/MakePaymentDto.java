package com.mkyong.model.dtos.Transaction;

import java.math.BigDecimal;

import com.mkyong.model.enums.TxType;

import lombok.Data;

@Data
public class MakePaymentDto {
    private String title;
    private BigDecimal amount;
    private String senderId;
    private String receiverId;
    private TxType type;
    private boolean ignoreDuplicateTx;
}
