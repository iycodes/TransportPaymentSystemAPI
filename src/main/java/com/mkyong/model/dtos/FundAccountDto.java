package com.mkyong.model.dtos;

import java.math.BigDecimal;

import com.mkyong.model.enums.TxType;

import lombok.Data;

@Data
public class FundAccountDto {
    private String fintech_tx_id;
    private String fintech_ref;
    private String userId;
    private BigDecimal amount;
    private String title;
    private String txRef;
    private TxType txType;

    public FundAccountDto(String fintech_tx_id_, String fintech_ref_, String userId_, String title_, BigDecimal amount_,
            String txRef_) {
        this.fintech_tx_id = fintech_tx_id_;
        this.fintech_ref = fintech_ref_;
        userId = userId_;
        title = title_;
        amount = amount_;
        txRef = txRef_;
        txType = TxType.funding;
    }

}
