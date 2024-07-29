package com.sparta.itsmine.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomRequestDto {

    private String roomId; // 방 번호
    private String fromUserId; // 채팅을 보낸 사람

}
