package com.example.world.user;

import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {

        this.value = value;
    }
    private String value;
}
