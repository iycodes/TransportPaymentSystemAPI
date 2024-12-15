package com.mkyong.model.dtos;

import lombok.Data;

@Data
public class UpdateTxWebhookDto {
    private String event;
    private WebhookData data;
    private String status;

}
