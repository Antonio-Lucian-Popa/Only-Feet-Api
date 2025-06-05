package com.asusoftware.only_feet_api.subscription.controller;

import com.asusoftware.only_feet_api.subscription.model.dto.CreateSubscriptionDto;
import com.asusoftware.only_feet_api.subscription.model.dto.SubscriptionDto;
import com.asusoftware.only_feet_api.subscription.service.SubscriptionService;
import com.asusoftware.only_feet_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    /**
     * Creează un abonament (activat după webhook Stripe).
     */
    @PostMapping
    public ResponseEntity<SubscriptionDto> subscribe(@RequestBody CreateSubscriptionDto dto,
                                                     @AuthenticationPrincipal Jwt principal) {
        UUID userId = userService.getUserByKeycloakId(principal);
        SubscriptionDto subscription = subscriptionService.createSubscription(dto, userId);
        return ResponseEntity.ok(subscription);
    }

    /**
     * Verifică dacă user-ul este abonat la un creator.
     */
    @GetMapping("/is-subscribed")
    public ResponseEntity<Boolean> isSubscribed(@RequestParam UUID creatorId,
                                                @AuthenticationPrincipal Jwt principal) {
        UUID userId = userService.getUserByKeycloakId(principal);
        boolean isSubscribed = subscriptionService.isUserSubscribed(userId, creatorId);
        return ResponseEntity.ok(isSubscribed);
    }

    /**
     * Returnează toate abonamentele active ale utilizatorului curent.
     */
    @GetMapping("/my")
    public ResponseEntity<List<SubscriptionDto>> mySubscriptions(@AuthenticationPrincipal Jwt principal) {
        UUID userId = userService.getUserByKeycloakId(principal);
        List<SubscriptionDto> list = subscriptionService.getUserSubscriptions(userId);
        return ResponseEntity.ok(list);
    }
}
