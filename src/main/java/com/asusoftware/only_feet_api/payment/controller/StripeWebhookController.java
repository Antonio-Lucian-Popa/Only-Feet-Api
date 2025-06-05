package com.asusoftware.only_feet_api.payment.controller;

import com.asusoftware.only_feet_api.subscription.service.SubscriptionService;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/webhooks/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final SubscriptionService subscriptionService;

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    @PostMapping
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);

            if ("customer.subscription.created".equals(event.getType())) {
                Subscription subscription = (Subscription) event.getDataObjectDeserializer()
                        .getObject().orElseThrow();
                subscriptionService.activateSubscriptionFromStripe(subscription);
            }

            return ResponseEntity.ok("Webhook received.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid payload: " + e.getMessage());
        }
    }
}
