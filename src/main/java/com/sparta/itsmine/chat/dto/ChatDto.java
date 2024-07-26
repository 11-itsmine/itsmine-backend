package com.sparta.itsmine.chat.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {

    private MessageType type;
    private String roomId; // 방 번호
    private String senderId; // 채팅을 보낸 사람
    private String message; // 메시지
    private LocalDateTime time;
}
