
package com.mkyong.model.dtos.Transaction;

import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;

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

    public TxDto(String id_, String senderId_, String receiverId_, LocalDateTime createdAt_, TxStatus status_,
            TxType type_) {
        id = id_;
        senderId = senderId_;
        receiverId = receiverId_;
        createdAt = createdAt_;
        status = status_;
        type = type_;

    }
}