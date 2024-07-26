package com.sparta.itsmine.chat.entity;

import java.util.HashMap;
import java.util.UUID;
import lombok.Data;

@Data
public class ChatRoom {

    private String roomId;
    private String roomName;
    private long userCount;
    private HashMap<String, String> userList = new HashMap<>();

    public ChatRoom create(String roomName) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.roomName = roomName;

        return chatRoom;
    }

}
