package com.sparta.itsmine.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {

    private MessageType messageType; // 메시지 타입
    private Long roomId; // 방 번호
    private Long senderId; // 채팅을 보낸 사람
    private String message; // 메시지

}