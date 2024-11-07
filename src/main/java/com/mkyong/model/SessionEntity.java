package com.mkyong.model;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Component
@Table(name = "sessions")
public class SessionEntity {
    @Id
    private String id;
    private String userId;
    private String refreshToken;
    private String ipAddress;
    private String fcmToken;
    private LocalDateTime createdAt = LocalDateTime.now();

    public SessionEntity() {
    }

    public SessionEntity(String userId_, String refreshToken_, String ipAddress_, String fcmToken_) {
        // this.createdAt = createdAt_;
        System.out.println("new session being created, id is " + userId_);
        this.id = "" + userId_ + LocalDateTime.now();
        this.userId = userId_;
        this.refreshToken = refreshToken_;
        this.fcmToken = fcmToken_;
    }

}
