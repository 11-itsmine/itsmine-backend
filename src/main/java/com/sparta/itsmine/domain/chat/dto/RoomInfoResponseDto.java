package com.sparta.itsmine.domain.chat.dto;

import com.sparta.itsmine.domain.chat.entity.ChatRoom;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class RoomInfoResponseDto {

    private final String roomId;
    private final List<UserChatInfoResponseDto> userChatInfos;

    public RoomInfoResponseDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.userChatInfos = chatRoom.getJoinChats().stream()
                .map(joinChat -> new UserChatInfoResponseDto(joinChat.getUser().getId(),
                        joinChat.getUser().getNickname()))
                .collect(Collectors.toList());
    }
}
