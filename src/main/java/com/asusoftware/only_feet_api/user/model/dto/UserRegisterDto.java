package com.asusoftware.only_feet_api.user.model.dto;

import com.asusoftware.only_feet_api.user.model.UserRole;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private UserRole role;        // CREATOR, USER, ADMIN
}