package com.asusoftware.only_feet_api.user.model.dto;

import com.asusoftware.only_feet_api.user.model.UserRole;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private UUID keycloakId;
    private String stripeAccountId;
}
