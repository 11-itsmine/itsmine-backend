package com.sparta.itsmine.chat.controller;

import com.sparta.itsmine.chat.dto.ChatDto;
import com.sparta.itsmine.chat.entity.ChatRoom;
import com.sparta.itsmine.chat.service.ChatService;
import com.sparta.itsmine.global.common.response.HttpResponseDto;
import com.sparta.itsmine.global.common.response.ResponseCodeEnum;
import com.sparta.itsmine.global.common.response.ResponseUtils;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private static final String CHAT_QUEUE_NAME = "chat.queue";
    private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final ChatService chatService;
    private final RabbitTemplate rabbitTemplate;

    @MessageMapping("chat.enter.{chatRoomId}")
    public void enterUser(@Payload ChatDto chatDto, @DestinationVariable String chatRoomId) {
        chatDto.setTime(LocalDateTime.now());
        chatDto.setMessage(chatDto.getSenderId() + "님 입장했습니다");
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chatDto);
    }

    @MessageMapping("chat.message.{chatRoomId}")
    public void sendMessage(@Payload ChatDto chatDto) {
        log.info("Chat : {}", chatDto);
        chatDto.setMessage(chatDto.getMessage());
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatDto.getRoomId(),
                chatDto);
    }

    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(ChatDto chatDto) {
        System.out.println("received : " + chatDto.getMessage());
    }

    @GetMapping("/chatroom")
    public ResponseEntity<HttpResponseDto> getChatRoom() {
        List<ChatRoom> chatRooms = chatService.findAllRoom();

        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_CREATE, chatRooms);
    }

    @PostMapping("/room/")
    public ResponseEntity<HttpResponseDto> createRoom(@RequestParam String name) {
        ChatRoom room = chatService.createChatRoom(name);
        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_CREATE, room);
    }

    @GetMapping("/userlist")
    private ResponseEntity<HttpResponseDto> userList(String roomId) {
        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_CREATE,
                chatService.getUserList(roomId));
    }
}
