package com.example.world.user;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }
    private String value;
}
