package com.sparta.itsmine.global.common.config;

import com.sparta.itsmine.global.security.JwtProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
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
    private final JwtProvider jwtProvider;
    String username;

    /*
     * 역할: 메시지가 실제로 전송되기 전에 호출됩니다.
     * 설명: 이 메서드에서 메시지를 수정하거나 검증할 수 있습니다.
     *       반환된 메시지가 실제로 전송됩니다. null을 반환하면 메시지는 전송되지 않습니다.
     * */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info("token : {} ", accessor.getFirstNativeHeader("Authorization"));
        // websocket 연결시 헤더의 jwt token 검증
        if (StompCommand.CONNECT == accessor.getCommand()) {

            jwtProvider.validateAccessToken(accessor.getFirstNativeHeader("token"));
        }
        return message;
//        try {
//            StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message,
//                    StompHeaderAccessor.class);
//
//            String authorizationHeader = String.valueOf(
//                    headerAccessor.getNativeHeader(AUTHORIZATION_HEADER));
//
//            StompCommand command = headerAccessor.getCommand();
//
//            if (command.equals(StompCommand.UNSUBSCRIBE) || command.equals(StompCommand.MESSAGE) ||
//                    command.equals(StompCommand.CONNECTED) || command.equals(StompCommand.SEND)) {
//                return message;
//            } else if (command.equals(StompCommand.ERROR)) {
//                throw new MessageDeliveryException("error");
//            }
//
//            if (authorizationHeader == null) {
//                log.info("chat header가 없는 요청입니다.");
//                throw new MalformedJwtException("jwt");
//            }
//
//            //token 분리
//            String token = "";
//            String authorizationHeaderStr = authorizationHeader.replace("[", "").replace("]", "");
//            if (authorizationHeaderStr.startsWith(BEARER_PREFIX)) {
//                token = authorizationHeaderStr.replace(BEARER_PREFIX, "");
//            } else {
//                log.error("Authorization 헤더 형식이 틀립니다. : {}", authorizationHeader);
//                throw new MalformedJwtException("jwt");
//            }
//
//            boolean isTokenValid = jwtProvider.validateAccessToken(token);
//
//            if (isTokenValid) {
//                this.setAuthentication(message, headerAccessor);
//            }
//        } catch (IllegalArgumentException e) {
//            log.error("JWT에러");
//            throw new MalformedJwtException("jwt");
//        } catch (MessageDeliveryException e) {
//            log.error("메시지 에러");
//            throw new MessageDeliveryException("error");
//        }
//        return message;
    }

    private void setAuthentication(Message<?> message, StompHeaderAccessor headerAccessor) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username, null, List.of(new SimpleGrantedAuthority("ENTER")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        headerAccessor.setUser(authentication);
    }
}