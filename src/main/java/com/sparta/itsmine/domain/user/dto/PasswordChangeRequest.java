package com.sparta.itsmine.domain.user.dto;

import com.sparta.itsmine.domain.user.utils.UserIdentity;
import lombok.Getter;

@Getter
public class PasswordChangeRequest implements UserIdentity {
    private String username;
    private String name;
    private String email;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
