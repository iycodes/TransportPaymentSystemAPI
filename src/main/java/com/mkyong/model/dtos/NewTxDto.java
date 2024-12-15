package com.mkyong.model.dtos;

import java.math.BigDecimal;

import com.mkyong.model.enums.TxType;

import lombok.Data;

@Data
public class NewTxDto {
    private String txRef;
    private String fintech_tx_id;
    private String fintech_ref;
    private String userId;
    private BigDecimal amount;
    private String title;
    // private String txRef;
    private TxType txType;
    private String merchant;

}
