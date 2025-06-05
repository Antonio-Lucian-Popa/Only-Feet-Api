package com.asusoftware.only_feet_api.user.controller;

import com.asusoftware.only_feet_api.user.model.dto.*;
import com.asusoftware.only_feet_api.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public UserDto register(@RequestBody @Valid UserRegisterDto dto) {
        return userService.register(dto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginDto dto) {
        return userService.login(dto);
    }

    @PostMapping("/refresh")
    public LoginResponseDto refresh(@RequestParam("refresh_token") String refreshToken) {
        return userService.refresh(refreshToken);
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public UserDto completeProfile(@RequestBody @Valid UpdateUserProfileDto dto,
                                   @AuthenticationPrincipal Jwt principal) {
        return userService.completeProfile(principal, dto);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public UserDto getCurrentUser(@AuthenticationPrincipal Jwt principal) {
        return userService.getCurrentUser(principal);
    }
}