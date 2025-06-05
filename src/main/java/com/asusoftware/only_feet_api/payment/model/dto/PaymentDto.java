package com.asusoftware.only_feet_api.payment.model.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private UUID id;
    private UUID userId;
    private UUID creatorId;
    private int amountCents;
    private String currency;
    private String status;
    private String stripePaymentIntentId;
    private String stripeSessionId;
    private LocalDateTime createdAt;
}
