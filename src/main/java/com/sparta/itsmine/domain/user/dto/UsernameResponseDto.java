package com.sparta.itsmine.domain.user.dto;

import lombok.Getter;

@Getter
public class UsernameResponseDto {

	private String nickname;

	public UsernameResponseDto(String nickname) {
		this.nickname = nickname;
	}
}
