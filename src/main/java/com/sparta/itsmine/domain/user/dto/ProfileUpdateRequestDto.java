package com.sparta.itsmine.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRequestDto {

	@Email
	private String email;
	@NotBlank(message = "Required Nickname")
	private String nickname;
	private String address;
}
