package com.asusoftware.only_feet_api.payment.repository;

import com.asusoftware.only_feet_api.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByUserId(UUID userId);
    List<Payment> findByCreatorId(UUID creatorId);
    Payment findByStripeSessionId(String sessionId);
}
