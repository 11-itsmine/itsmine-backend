package com.sparta.itsmine.domain.chat.dto;

import lombok.Data;

@Data
public class UserChatInfoResponseDto {

    private final Long userId;
    private final String nickname;

    public UserChatInfoResponseDto(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}
