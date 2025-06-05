package com.asusoftware.only_feet_api.payment.controller;

import com.asusoftware.only_feet_api.payment.service.StripePaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final StripePaymentService stripePaymentService;

    @PostMapping("/session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestParam UUID creatorId,
                                                                     @RequestBody Map<String, Object> payload,
                                                                     Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        String checkoutUrl = stripePaymentService.createCheckoutSession(userId, creatorId);
        return ResponseEntity.ok(Map.of("url", checkoutUrl));
    }
}
