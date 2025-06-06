package com.asusoftware.only_feet_api.subscription.service;

import com.asusoftware.only_feet_api.subscription.model.SubscriptionEntity;
import com.asusoftware.only_feet_api.subscription.model.dto.CreateSubscriptionDto;
import com.asusoftware.only_feet_api.subscription.model.dto.SubscriptionDto;
import com.asusoftware.only_feet_api.subscription.repository.SubscriptionRepository;
import com.asusoftware.only_feet_api.user.model.User;
import com.asusoftware.only_feet_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.stripe.model.Subscription;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public SubscriptionDto createSubscription(CreateSubscriptionDto dto, UUID userId) {
        if (subscriptionRepository.existsByUserIdAndCreatorId(userId, dto.getCreatorId())) {
            throw new RuntimeException("Ești deja abonat la acest creator.");
        }

        // Validare creator
        User creator = userRepository.findById(dto.getCreatorId())
                .orElseThrow(() -> new RuntimeException("Creatorul nu există."));
        if (!"CREATOR".equals(creator.getRole().name())) {
            throw new RuntimeException("Acest utilizator nu este creator.");
        }

        SubscriptionEntity subscriptionEntity = SubscriptionEntity.builder()
                .userId(userId)
                .creatorId(dto.getCreatorId())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1)) // abonament pe 1 lună
                .active(true)
                .stripeSubscriptionId(dto.getStripeSubscriptionId()) // setat după webhook
                .build();

        return mapper.map(subscriptionRepository.save(subscriptionEntity), SubscriptionDto.class);
    }

    public boolean isUserSubscribed(UUID userId, UUID creatorId) {
        return subscriptionRepository.existsByUserIdAndCreatorIdAndActiveTrue(userId, creatorId);
    }

    public List<SubscriptionDto> getUserSubscriptions(UUID userId) {
        return subscriptionRepository.findByUserIdAndActiveTrue(userId).stream()
                .map(sub -> mapper.map(sub, SubscriptionDto.class))
                .collect(Collectors.toList());
    }

    public void deactivateSubscription(UUID subscriptionId) {
        SubscriptionEntity sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Abonamentul nu există."));
        sub.setActive(false);
        subscriptionRepository.save(sub);
    }

    public void cancelExpiredSubscriptions() {
        List<SubscriptionEntity> expired = subscriptionRepository.findByEndDateBeforeAndActiveTrue(LocalDateTime.now());
        expired.forEach(sub -> sub.setActive(false));
        subscriptionRepository.saveAll(expired);
    }

    /**
     * Dezactivează toate abonamentele expirate.
     */
    @Transactional
    public void deactivateExpiredSubscriptions() {
        List<SubscriptionEntity> expired = subscriptionRepository.findByEndDateBeforeAndActiveTrue(LocalDateTime.now());

        for (SubscriptionEntity s : expired) {
            s.setActive(false);
        }

        subscriptionRepository.saveAll(expired);
    }


    /**
     * Activează un abonament primit prin Stripe Webhook.
     */
    @Transactional
    public void activateSubscriptionFromStripe(Subscription stripeSubscription) {
        // Extragem ID-urile relevante
        String customerId = stripeSubscription.getCustomer();
        String stripeSubscriptionId = stripeSubscription.getId();
        Long startTimestamp = stripeSubscription.getStartDate();
        Long endTimestamp = stripeSubscription.getCurrentPeriodEnd();

        // Caută userul după Stripe customer ID
        User user = userRepository.findByStripeAccountId(customerId)
                .orElseThrow(() -> new RuntimeException("User cu stripeAccountId inexistent: " + customerId));

        // Caută creatorul (pentru simplitate presupunem că metadatele Stripe includ ID-ul lui)
        String creatorIdRaw = stripeSubscription.getMetadata().get("creatorId");
        if (creatorIdRaw == null) {
            throw new RuntimeException("Abonamentul nu conține creatorId în metadata.");
        }

        UUID creatorId = UUID.fromString(creatorIdRaw);

        // Salvează abonamentul
        SubscriptionEntity subscription = SubscriptionEntity.builder()
                .userId(user.getId())
                .creatorId(creatorId)
                .startDate(LocalDateTime.ofEpochSecond(startTimestamp, 0, java.time.ZoneOffset.UTC))
                .endDate(LocalDateTime.ofEpochSecond(endTimestamp, 0, java.time.ZoneOffset.UTC))
                .active(true)
                .stripeSubscriptionId(stripeSubscriptionId)
                .build();

        subscriptionRepository.save(subscription);
    }

}
