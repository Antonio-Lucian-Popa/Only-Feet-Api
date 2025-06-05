package com.asusoftware.only_feet_api.payment.controller;

import com.asusoftware.only_feet_api.payment.model.dto.SetCreatorPriceDto;
import com.asusoftware.only_feet_api.payment.service.StripeService;
import com.asusoftware.only_feet_api.user.model.User;
import com.asusoftware.only_feet_api.user.model.UserRole;
import com.asusoftware.only_feet_api.user.repository.UserRepository;
import com.asusoftware.only_feet_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/creator")
@RequiredArgsConstructor
public class CreatorController {

    private final StripeService stripeService;
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/set-price")
    public ResponseEntity<?> setPrice(@RequestBody SetCreatorPriceDto dto,
                                      @AuthenticationPrincipal Jwt principal) {
       User creator = userService.getCurrentUserEntity(principal);

        if (!creator.getRole().equals(UserRole.CREATOR)) {
            return ResponseEntity.badRequest().body("Doar creatorii pot seta prețul.");
        }

        // Creează Price în Stripe și salvează ID-ul
        String priceId = stripeService.createPriceForCreator(dto.getAmountCents(), dto.getCurrency());
        creator.setStripePriceId(priceId);
        userRepository.save(creator);

        return ResponseEntity.ok("Preț setat cu succes.");
    }
}

