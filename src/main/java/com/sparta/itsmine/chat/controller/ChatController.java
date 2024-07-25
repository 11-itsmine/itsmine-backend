package com.sparta.itsmine.chat.controller;


import static com.sparta.itsmine.global.common.response.ResponseCodeEnum.CHAT_SUCCESS_CREATE;

import com.sparta.itsmine.chat.dto.ChatMessageDto;
import com.sparta.itsmine.chat.entity.ChatRoom;
import com.sparta.itsmine.chat.entity.Message;
import com.sparta.itsmine.chat.service.ChatService;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.response.HttpResponseDto;
import com.sparta.itsmine.global.common.response.ResponseUtils;
import com.sparta.itsmine.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    // stomp 방식으로 메세지를 주고 받기 위해 의존성 주입
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    private final ChatService chatService;

    @MessageMapping("/rooms/{roomId}") //여기로 전송되면 메서드 호출 -> WebSocketConfig prefixes 에서 적용한건 앞에 생략
    @SendTo("/rooms/{roomId}")
    //구독하고 있는 장소로 메시지 전송 (목적지)  -> WebSocketConfig Broker 에서 적용한건 앞에 붙어줘야됨
    public void test(@DestinationVariable Long roomId, ChatMessageDto requestDto,
            SimpMessageHeaderAccessor headerAccessor) {
        log.info("메세지 전송 하니?");
//        User user = userDetails.getUser();
        //채팅 저장
        User user = (User) headerAccessor.getUser();
        simpMessageSendingOperations.convertAndSend("/sub/rooms/" + roomId,
                requestDto.getMessage());
        Message message = chatService.createMessage(roomId, requestDto, user);
//        return ChatMessageDto.builder()
//                .roomId(roomId)
//                .messageType(MessageType.TALK)
//                .senderId(user.getId())
//                .message(message.getContent())
//                .build();
    }

    /**
     * 채팅방 참여하기
     * @param roomId 채팅방 id
     */
//    @GetMapping("/{roomId}")
//    public String joinRoom(@PathVariable(required = false) Long roomId, Model model) {
//        List<Chat> chatList = chatService.findAllChatByRoomId(roomId);
//
//        model.addAttribute("roomId", roomId);
//        model.addAttribute("chatList", chatList);
//        return "room";
//    }

    /**
     * 채팅방 리스트 보기
     */
    @GetMapping("/room/list")
    public String roomList(Model model
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        List<ChatRoom> roomList = chatService.findAllRoom();
        model.addAttribute("roomList", roomList);
        return "roomList";
    }

    /**
     * 채팅방 만들기
     */
    @PostMapping("/room")
    public ResponseEntity<HttpResponseDto> createRoom(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long roomId = chatService.createRoom().getId();
        return ResponseUtils.of(CHAT_SUCCESS_CREATE, "방 번호 : " + roomId);
    }

    /**
     * 방만들기 폼
     */
    @GetMapping("/room/form")
    public String roomForm() {
        return "roomForm";
    }


}
