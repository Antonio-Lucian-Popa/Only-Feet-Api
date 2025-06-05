package com.asusoftware.only_feet_api.user.service;

import com.asusoftware.only_feet_api.config.KeycloakService;
import com.asusoftware.only_feet_api.user.model.User;
import com.asusoftware.only_feet_api.user.model.dto.*;
import com.asusoftware.only_feet_api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;
    private final ModelMapper mapper;

    @Transactional
    public UserDto register(UserRegisterDto dto) {
        UUID keycloakId = UUID.fromString(keycloakService.createKeycloakUser(dto));

        User user = User.builder()
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .keycloakId(keycloakId)
                .role(dto.getRole())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);
        return mapper.map(user, UserDto.class);
    }

    public LoginResponseDto login(LoginDto dto) {
        return keycloakService.loginUser(dto);
    }

    public LoginResponseDto refresh(String refreshToken) {
        return keycloakService.refreshToken(refreshToken);
    }

    public UserDto completeProfile(Jwt principal, UpdateUserProfileDto dto) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean changed = false;

        if (user.getFirstName() == null && dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
            changed = true;
        }
        if (user.getLastName() == null && dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
            changed = true;
        }
        if (user.getRole() == null && dto.getRole() != null) {
            user.setRole(dto.getRole());
            changed = true;
        }

        if (changed) {
            user.setUpdatedAt(LocalDateTime.now());
            user = userRepository.save(user);
        }

        return mapper.map(user, UserDto.class);
    }

    public UserDto getCurrentUser(Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.map(user, UserDto.class);
    }

    public User getCurrentUserEntity(Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UUID getUserByKeycloakId(Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
    }

}

