package com.sparta.itsmine.domain.user.utils;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("USER"),
    MANAGER("MANAGER");

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }
}
