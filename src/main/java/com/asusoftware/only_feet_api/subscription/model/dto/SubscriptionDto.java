package com.asusoftware.only_feet_api.subscription.model.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {
    private UUID id;
    private UUID userId;
    private UUID creatorId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
    private String stripeSubscriptionId;
}

