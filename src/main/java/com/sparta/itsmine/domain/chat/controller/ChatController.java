package com.sparta.itsmine.domain.chat.controller;

import com.sparta.itsmine.domain.chat.dto.MessageRequestDto;
import com.sparta.itsmine.domain.chat.service.ChatService;
import jakarta.jms.Queue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;
    private final JmsTemplate jmsTemplate;
    private final Queue queue; // Queue 빈 주입

    /**
     * 클라이언트가 /app/chat.message.{chatRoomId}로 메시지를 전송할 때 호출됩니다.
     * 메시지를 ActiveMQ로 전송합니다.
     *
     * @param requestDto 메시지 보낼 정보를 담고 있는 DTO
     */
    @MessageMapping("chat.message.{chatRoomId}")
    public void sendMessage(@Payload MessageRequestDto requestDto) {
        log.info("Sending message: {}", requestDto.getMessage());

        // 메시지를 ActiveMQ의 특정 큐로 전송
        jmsTemplate.convertAndSend(queue, requestDto);
    }

    /**
     * ActiveMQ에서 메시지를 수신하여 WebSocket 클라이언트로 전달합니다.
     * 수신한 메시지는 특정 채팅 방의 구독자에게 전송됩니다.
     *
     * @param requestDto 수신한 메시지를 담고 있는 DTO
     */
    @JmsListener(destination = "${activemq.queue.name}") // Queue 이름을 외부 설정으로부터 가져옴
    public void receiveMessage(MessageRequestDto requestDto) {
        log.info("Received message: {}", requestDto.getMessage());

        // 메시지를 데이터베이스에 저장하는 서비스 호출
        chatService.saveMessage(requestDto);

        // WebSocket 클라이언트에게 메시지 전송
        // messagingTemplate.convertAndSend("/topic/room." + requestDto.getRoomId(), requestDto);
        // 메시지 전송 로직 추가
    }
}