package com.sparta.itsmine.chat.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto {

    private String message;
    private Long fromUserId;
    private String roomId;
    private LocalDateTime time;

}
