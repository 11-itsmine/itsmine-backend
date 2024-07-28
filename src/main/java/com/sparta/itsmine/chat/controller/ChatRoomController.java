package com.sparta.itsmine.chat.controller;

import com.sparta.itsmine.chat.dto.ChatRoomRequestDto;
import com.sparta.itsmine.chat.dto.RoomInfoResponseDto;
import com.sparta.itsmine.chat.dto.UserRequestDto;
import com.sparta.itsmine.chat.entity.Message;
import com.sparta.itsmine.chat.service.ChatService;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.response.HttpResponseDto;
import com.sparta.itsmine.global.common.response.ResponseCodeEnum;
import com.sparta.itsmine.global.common.response.ResponseUtils;
import com.sparta.itsmine.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/chatrooms")
public class ChatRoomController {

    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<HttpResponseDto> getChatRoom(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        List<RoomInfoResponseDto> responseDtos = chatService.findAllRoom(userId);

        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_CREATE, responseDtos);
    }

    @PostMapping
    public ResponseEntity<HttpResponseDto> createRoom(@RequestBody UserRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        RoomInfoResponseDto responseDto = chatService.createChatRoom(user, requestDto.getUserId());
        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_CREATE, responseDto);
    }

    @GetMapping("/chat")
    public ResponseEntity<HttpResponseDto> getChat(@RequestBody ChatRoomRequestDto requestDto) {
        String roomId = requestDto.getRoomId();
        List<Message> messageList = chatService.getMessageList(roomId);
        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_CREATE, messageList);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<HttpResponseDto> leaveUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable String roomId
    ) {
        User user = userDetails.getUser();
        chatService.leaveUser(user, roomId);
        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_CREATE);
    }

}
