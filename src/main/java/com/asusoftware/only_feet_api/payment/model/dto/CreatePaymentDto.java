package com.asusoftware.only_feet_api.payment.model.dto;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentDto {
    private UUID creatorId;
    private int amountCents;
    private String currency;
}