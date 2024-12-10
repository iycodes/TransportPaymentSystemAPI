
package com.mkyong.model.dtos.Transaction;

import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;

import com.mkyong.model.TransactionEntity;
import com.mkyong.model.enums.TxStatus;
import com.mkyong.model.enums.TxType;

import lombok.Data;

/**
 * TxDto
 */
@Data
public class TxDto {

    private String id;
    private String senderId;
    private String receiverId;
    private LocalDateTime createdAt;
    private TxStatus status;
    private TxType type;
    private String merchant;

    public TxDto() {

    }

    public TxDto(String id_, String senderId_, String receiverId_, LocalDateTime createdAt_, TxStatus status_,
            TxType type_, String merchant_) {
        id = id_;
        senderId = senderId_;
        receiverId = receiverId_;
        createdAt = createdAt_;
        status = status_;
        type = type_;
        merchant = merchant_;

    }

    public TxDto funding(String id_, String userId_, String merchant_) {
        String id = TransactionEntity.createId(userId_, "Funding");
        final LocalDateTime createdAt = LocalDateTime.now();
        return new TxDto(id, userId_, "Funding", createdAt, TxStatus.pending, TxType.funding, merchant_);
    }
}