package com.sparta.itsmine.domain.chat.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto implements Serializable {

    private String message;
    private Long fromUserId;
    private String roomId;
    private LocalDateTime time;

}
