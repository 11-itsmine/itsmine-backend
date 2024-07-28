package com.sparta.itsmine.chat.entity;

import com.sparta.itsmine.chat.dto.MessageRequestDto;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@Document(collection = "message")
public class Message {

    @Id
    private String Id;

    private String roomId;

    private Long fromUserId;

    private String massage;

    private LocalDateTime time;

    public Message(MessageRequestDto requestDto) {
        this.roomId = requestDto.getRoomId();
        this.fromUserId = requestDto.getFromUserId();
        this.massage = requestDto.getMessage();
        this.time = requestDto.getTime();
    }

}
