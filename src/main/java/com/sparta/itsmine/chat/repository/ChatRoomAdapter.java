package com.sparta.itsmine.chat.repository;

import com.sparta.itsmine.chat.entity.ChatRoom;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRoomAdapter {

    private final ChatRoomsRepository chatRoomRepository;

    public ChatRoom findById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을수 없습니다"));
    }

    public List<ChatRoom> findAll() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom save(ChatRoom chatroom) {
        return chatRoomRepository.save(chatroom);
    }


}
