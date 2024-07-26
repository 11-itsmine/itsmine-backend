package com.sparta.itsmine.chat.controller;

import com.sparta.itsmine.chat.entity.ChatRoom;
import com.sparta.itsmine.chat.service.ChatService;
import com.sparta.itsmine.global.common.response.HttpResponseDto;
import com.sparta.itsmine.global.common.response.ResponseCodeEnum;
import com.sparta.itsmine.global.common.response.ResponseUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/chatroom")
public class ChatRoomController {

    private final ChatService chatService;

    @GetMapping
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
