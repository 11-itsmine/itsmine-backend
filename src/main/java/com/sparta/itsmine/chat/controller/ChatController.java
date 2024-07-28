package com.sparta.itsmine.chat.controller;

import com.sparta.itsmine.chat.dto.MessageRequestDto;
import com.sparta.itsmine.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private static final String CHAT_QUEUE_NAME = "chat.queue";
    private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final ChatService chatService;
    private final RabbitTemplate rabbitTemplate;


    @MessageMapping("chat.message.{chatRoomId}")
    public void sendMessage(@Payload MessageRequestDto requestDto) {
        log.info("Chat : {}", requestDto);
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + requestDto.getRoomId(),
                requestDto);
    }

    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(MessageRequestDto requestDto) {
        log.info("received : {}", requestDto.getMessage());
        chatService.saveMessage(requestDto);
    }
}
