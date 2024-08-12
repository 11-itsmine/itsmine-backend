package com.sparta.itsmine.domain.chat.dto;

import com.sparta.itsmine.domain.chat.entity.ChatRoom;
import com.sparta.itsmine.domain.chat.entity.ChatStatus;
import lombok.Data;

@Data
public class RoomInfoResponseDto {

    private final String roomId;
    private final Long productId;
    private final String prductName;
    private final Long userDetailId;
    private final Long fromUserId;
    private final String fromUsername;
    private final String fromUserNickname;
    private final ChatStatus fromUserStatus;
    private final Long toUserId;
    private final String toUsername;
    private final String toUserNickname;
    private final ChatStatus toUserStatus;
    //private final List<UserChatInfoResponseDto> userChatInfos;

    public RoomInfoResponseDto(ChatRoom chatRoom, Long userDetailId) {
        this.roomId = chatRoom.getRoomId();
        this.productId = chatRoom.getProduct().getId();
        this.prductName = chatRoom.getProduct().getProductName();
        this.userDetailId = userDetailId;
        this.fromUserId = chatRoom.getFromUser().getId();
        this.fromUsername = chatRoom.getFromUser().getUsername();
        this.fromUserNickname = chatRoom.getFromUser().getNickname();
        this.fromUserStatus = chatRoom.getFromUserStatus();
        this.toUserId = chatRoom.getToUser().getId();
        this.toUsername = chatRoom.getToUser().getUsername();
        this.toUserNickname = chatRoom.getToUser().getNickname();
        this.toUserStatus = chatRoom.getToUserStatus();
    }
}
