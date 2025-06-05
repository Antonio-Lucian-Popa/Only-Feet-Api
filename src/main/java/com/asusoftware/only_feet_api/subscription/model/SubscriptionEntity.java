package com.asusoftware.only_feet_api.subscription.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "subscription")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId; // Refers to User.id

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId; // Refers to User.id

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    private Boolean active;

    @Column(name = "stripe_subscription_id")
    private String stripeSubscriptionId;
}
