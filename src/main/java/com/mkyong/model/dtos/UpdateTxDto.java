package com.mkyong.model.dtos;

import java.math.BigDecimal;

import com.mkyong.model.enums.TxStatus;
import com.mkyong.model.enums.TxType;

import lombok.Data;

@Data
public class UpdateTxDto {
    private String txId;
    private String fintech_tx_id;
    // private String userId;
    private BigDecimal amount;
    // private String title;
    private TxStatus txStatus;
    // private TxType type;

    public UpdateTxDto(String txId_, BigDecimal amount_, TxStatus txStatus_, String fintech_tx_id_) {
        this.amount = amount_;
        this.txId = txId_;
        this.txStatus = txStatus_;
        this.fintech_tx_id = fintech_tx_id_;
    }

    public UpdateTxDto fromWebHook(WebhookData data) {
        switch (data.getStatus()) {
            case "succesful":
                return new UpdateTxDto(data.getTx_ref(), data.getAmount_settled(), TxStatus.pending, data.getId());
            case "failed":
                return new UpdateTxDto(data.getTx_ref(), data.getAmount_settled(), TxStatus.failed, data.getId());
            default:
                return new UpdateTxDto(data.getTx_ref(), data.getAmount_settled(), TxStatus.pending, data.getId());
        }

    }

}
