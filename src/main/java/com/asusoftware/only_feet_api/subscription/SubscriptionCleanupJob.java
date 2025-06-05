package com.asusoftware.only_feet_api.subscription;

import com.asusoftware.only_feet_api.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionCleanupJob {

    private final SubscriptionService subscriptionService;

    @Scheduled(cron = "0 0 * * * *") // La fiecare oră
    public void deactivateExpiredSubscriptions() {
        subscriptionService.deactivateExpiredSubscriptions();
    }
}