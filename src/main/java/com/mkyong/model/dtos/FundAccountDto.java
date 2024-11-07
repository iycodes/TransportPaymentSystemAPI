package com.mkyong.model.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FundAccountDto {
    private String userId;
    private BigDecimal amount;

}
