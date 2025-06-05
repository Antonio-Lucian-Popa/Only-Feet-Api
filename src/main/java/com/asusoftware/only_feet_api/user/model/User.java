package com.asusoftware.only_feet_api.user.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    private String firstName;
    private String lastName;

    @Column(nullable = false, unique = true, name = "keycloak_id")
    private UUID keycloakId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "stripe_account_id")
    private String stripeAccountId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}