package com.sparta.itsmine.domain.user.dto;

import lombok.Getter;

@Getter
public class PasswordChangeRequest {
    private String username;
    private String name;
    private String email;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
