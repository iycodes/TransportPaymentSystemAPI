package com.mkyong.model;

import jakarta.persistence.*;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Userr {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private BigDecimal balance;
    private LocalDate createdAt = LocalDate.now();

    // for JPA only, no use
    public Userr() {
    }

    @Override
    public String toString() {
        return "Userr{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", createdAt=" + createdAt +
                '}';
    }
    public Userr(String name, BigDecimal balance ) {
        this.name = name;
        this.balance = balance;
    }

}


