package com.mkyong.model.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class WebhookData {
    private BigDecimal charged_amount;
    private BigDecimal app_fee;
    private BigDecimal merchant_fee;
    private String status;
    private String payment_type;
    private String tx_ref;
    private String flw_ref;
}