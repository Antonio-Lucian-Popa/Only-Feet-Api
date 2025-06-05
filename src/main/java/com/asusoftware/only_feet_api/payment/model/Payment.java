package com.asusoftware.only_feet_api.payment.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;

    @Column(name = "amount_cents", nullable = false)
    private Integer amountCents;

    @Column(nullable = false)
    private String currency;

    @Column(name = "stripe_payment_intent_id")
    private String stripePaymentIntentId;

    @Column(name = "stripe_session_id")
    private String stripeSessionId;

    @Column(nullable = false)
    private String status; // PENDING, PAID, FAILED

    private LocalDateTime createdAt;
}
