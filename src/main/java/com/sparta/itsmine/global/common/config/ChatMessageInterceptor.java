package com.sparta.itsmine.global.common.config;

import com.sparta.itsmine.domain.chat.entity.ChatRoom;
import com.sparta.itsmine.domain.chat.entity.ChatStatus;
import com.sparta.itsmine.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageInterceptor implements ChannelInterceptor {

    private final ChatService chatService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(message);
        Long userId = Long.parseLong(accessor.getUser().getName());
        ChatRoom chatRoom = chatService.isBlock(userId);
        if (chatRoom.getFromUserStatus().equals(ChatStatus.BLOCK)) {
            throw new IllegalStateException("특정한 사유로 벤 처리 당한 유저 입니다.");
        }
        return message;
    }
}