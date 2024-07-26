package com.sparta.itsmine.global.common.config;

import com.sparta.itsmine.chat.service.ChatService;
import com.sparta.itsmine.global.security.JwtProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ChatPreHandler implements ChannelInterceptor {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final ChatService chatService;
    private final JwtProvider jwtProvider;
    String username;

    /*
     * 역할: 메시지가 실제로 전송되기 전에 호출됩니다.
     * 설명: 이 메서드에서 메시지를 수정하거나 검증할 수 있습니다.
     *       반환된 메시지가 실제로 전송됩니다. null을 반환하면 메시지는 전송되지 않습니다.
     * */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        return message;
    }

    private void setAuthentication(Message<?> message, StompHeaderAccessor headerAccessor) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username, null, List.of(new SimpleGrantedAuthority("ENTER")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        headerAccessor.setUser(authentication);
    }
}