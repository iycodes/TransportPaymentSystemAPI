package com.mkyong.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.mkyong.model.enums.TxStatus;
import com.mkyong.model.enums.TxType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Component
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @Column(unique = true)
    private String id;
    private String title;
    private String senderId;
    private String receiverId;
    private BigDecimal amount;

    private LocalDateTime createdAt = LocalDateTime.now();
    private TxStatus status;
    private TxType type;

    public TransactionEntity() {
    }

    public TransactionEntity(String id_, String title_, String senderId_, String receiverId_, BigDecimal amount_,
            TxStatus status_,
            TxType type_) {
        this.id = id_;
        this.title = title_;
        this.senderId = senderId_;
        this.receiverId = receiverId_;
        this.amount = amount_;
        this.status = status_;
        this.type = type_;

    }

    public String toString() {
        return "id is" + id + "sender id is " + senderId + "receiver id is " + receiverId
                + "amount is " + amount + "transaction was made on " + createdAt;
    }

    public static String createId(String senderId, String receiverId) {
        ;
        return "" + senderId + "-" + receiverId + "-" + LocalDateTime.now();

    }

}
