package com.mkyong.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class BlacklistedTokenEntity {
    @Id
    private String token;

    public BlacklistedTokenEntity() {
    }

    public BlacklistedTokenEntity(String token_) {
        this.token = token_;
    }
}
