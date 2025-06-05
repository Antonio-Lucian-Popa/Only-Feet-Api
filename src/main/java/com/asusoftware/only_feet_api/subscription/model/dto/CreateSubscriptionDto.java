package com.asusoftware.only_feet_api.subscription.model.dto;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubscriptionDto {
    private UUID creatorId;
    private String stripeSubscriptionId; // generat de Stripe după plata cu succes
}

