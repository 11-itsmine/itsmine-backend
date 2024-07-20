package com.sparta.itsmine.domain.user.dto;

import com.sparta.itsmine.domain.user.entity.User;

import lombok.Getter;

@Getter
public class UserResponseDto {

	private String username;
	private String name;
	private String nickname;
	private String email;
	private String address;

	public UserResponseDto(User user) {
		this.username = user.getUsername();
		this.name = user.getName();
		this.nickname = user.getNickname();
		this.email = user.getEmail();
		this.address = user.getAddress();
	}
}
