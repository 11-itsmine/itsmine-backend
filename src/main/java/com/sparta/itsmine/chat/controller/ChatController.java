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


    /**
     * 웹소켓 통신을 할때 메세지를 보냅니다.
     * <p>
     * 메시지를 받을 공간 : /exchange/chat.exchange/room.{roomId},보내는 방식 : /pub/chat.message.{roomId}
     *
     * @param requestDto 메시지 보낼 정보를 담았습니다 누가 보냈는지 방의 ID가 뭔지 등입니다.
     */
    @MessageMapping("chat.message.{chatRoomId}")
    public void sendMessage(@Payload MessageRequestDto requestDto) {
        log.info("Chat : {}", requestDto);
        chatService.getUserCount(requestDto.getRoomId());
        //long userCount = chatService.getUserCount(requestDto.getRoomId());

        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + requestDto.getRoomId(),
                requestDto);
    }

    /**
     * 메시지 전송 큐 담당하는 곳입니다.. 메시지가 제대로 갔는지 안갔는지 로그를 통해 확인할수 있습니다.
     * <p>
     * 여기에서 mongoDB 저장을 합니다.
     */
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(MessageRequestDto requestDto) {
        log.info("received : {}", requestDto.getMessage());
        chatService.saveMessage(requestDto);
    }
}
