package com.mkyong.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Component
@Table(name = "verification_codes")
public class VerificationCodeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private int code;
    @Column(unique = true)
    private String email;
    private LocalDateTime expiresIn = LocalDateTime.now().plusMinutes(30);

    public VerificationCodeEntity() {
    }

    public VerificationCodeEntity(int code_, String email_) {
        code = code_;
        email = email_;

    }
}