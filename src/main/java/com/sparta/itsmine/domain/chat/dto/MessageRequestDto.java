package com.sparta.itsmine.domain.chat.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDto implements Serializable {
    private UUID messageId;
    private String message;
    private Long fromUserId;
    private String roomId;
    private LocalDateTime time;

}
