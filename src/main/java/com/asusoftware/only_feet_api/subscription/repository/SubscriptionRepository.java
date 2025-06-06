package com.asusoftware.only_feet_api.subscription.repository;

import com.asusoftware.only_feet_api.subscription.model.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {
    Optional<SubscriptionEntity> findByUserIdAndCreatorId(UUID userId, UUID creatorId);
    List<SubscriptionEntity> findByUserId(UUID userId);
    List<SubscriptionEntity> findByCreatorId(UUID creatorId);

    boolean existsByUserIdAndCreatorId(UUID userId, UUID creatorId);
    boolean existsByUserIdAndCreatorIdAndActiveTrue(UUID userId, UUID creatorId);

    List<SubscriptionEntity> findByUserIdAndActiveTrue(UUID userId);

    List<SubscriptionEntity> findByEndDateBeforeAndActiveTrue(LocalDateTime date);

    Optional<SubscriptionEntity> findByStripeSubscriptionId(String stripeSubscriptionId);
}