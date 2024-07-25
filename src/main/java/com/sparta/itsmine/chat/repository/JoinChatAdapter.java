package com.sparta.itsmine.chat.repository;

import com.sparta.itsmine.chat.entity.JoinChat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JoinChatAdapter {

    private final JoinChatRepository joinChatRepository;

    // 임시 예외 처리
    public JoinChat findByUserIdAndChatRoomId(Long userId, Long roomId) {
        return joinChatRepository.findByUserIdAndChatRoomId(userId, roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을수 없습니다."));
    }
}
