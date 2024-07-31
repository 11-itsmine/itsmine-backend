package com.sparta.itsmine.domain.chat.dto;

import com.sparta.itsmine.domain.chat.entity.ChatRoom;
import com.sparta.itsmine.domain.chat.entity.ChatStatus;
import lombok.Data;

@Data
public class RoomInfoResponseDto {

    private final String roomId;
    private final Long fromUserId;
    private final String fromUserNickname;
    private final ChatStatus fromUserStatus;
    private final Long toUserId;
    private final String toUserNickname;
    private final ChatStatus toUserStatus;
    //private final List<UserChatInfoResponseDto> userChatInfos;

    public RoomInfoResponseDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.fromUserId = chatRoom.getFromUser().getId();
        this.fromUserNickname = chatRoom.getFromUser().getNickname();
        this.fromUserStatus = chatRoom.getToUserStatus();
        this.toUserId = chatRoom.getToUser().getId();
        this.toUserNickname = chatRoom.getToUser().getNickname();
        this.toUserStatus = chatRoom.getToUserStatus();
    }
}
