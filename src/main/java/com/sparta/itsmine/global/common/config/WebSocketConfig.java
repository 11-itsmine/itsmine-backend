package com.sparta.itsmine.global.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${spring.activemq.user:itsmine1010}")
    private String activeUser;
    @Value("${spring.activemq.password:itsmine101010}")
    private String activePwd;
    @Value("${spring.activemq.broker-url}")
    private String activeHost;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setApplicationDestinationPrefixes("/pub");       //클라이언트에서 보낸 메세지를 받을 prefix
//        registry.enableSimpleBroker("/sub");    //해당 주소를 구독하고 있는 클라이언트들에게 메세지 전달
        registry.enableStompBrokerRelay("/exchange")
                .setRelayHost("b-58f9491d-c8de-422c-8b11-4a18f612ec43-1.mq.ap-northeast-2.amazonaws.com")
                .setRelayPort(61614) // STOMP SSL 포트
                .setClientLogin(activeUser)
                .setClientPasscode(activePwd);

        registry.setPathMatcher(new AntPathMatcher("."));
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000/**")
                .withSockJS();//SockJS 연결 주소

        // 주소 : ws://localhost:8080/ws
    }

}