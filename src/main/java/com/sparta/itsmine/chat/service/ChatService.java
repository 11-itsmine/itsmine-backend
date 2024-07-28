package com.sparta.itsmine.chat.service;

import com.sparta.itsmine.chat.dto.MessageRequestDto;
import com.sparta.itsmine.chat.dto.RoomInfoResponseDto;
import com.sparta.itsmine.chat.entity.ChatRoom;
import com.sparta.itsmine.chat.entity.JoinChat;
import com.sparta.itsmine.chat.entity.Message;
import com.sparta.itsmine.chat.repository.ChatRoomRepository;
import com.sparta.itsmine.chat.repository.JoinChatRepository;
import com.sparta.itsmine.chat.repository.MessageRepository;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final JoinChatRepository joinChatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    //private Map<String, ChatRoom> chatRoomMap = new LinkedHashMap<>();

    public List<RoomInfoResponseDto> findAllRoom(Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByFromUserId(userId);
        return chatRooms.stream()
                .map(RoomInfoResponseDto::new).collect(Collectors.toList());
    }

    public RoomInfoResponseDto createChatRoom(User user, Long userId) {
        User productUser = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 존재 하지 않습니다.")
        );

        ChatRoom chatRoom = new ChatRoom(user, productUser);

        log.info("chatRoom : {} ", chatRoom.getFromUser());

        chatRoomRepository.save(chatRoom);
        chatRoomRepository.flush();
        return new RoomInfoResponseDto(chatRoom);
    }

    public List<Message> getMessageList(String roomId) {
        return messageRepository.findAllByRoomId(roomId);
    }

    public void leaveUser(User user, String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new IllegalArgumentException("없는 채팅방 입니다")
        );
        JoinChat joinChat = joinChatRepository.findByUserIdAndChatRoomId(user.getId(),
                        chatRoom.getId())
                .orElseThrow(() -> new IllegalArgumentException("이미 채팅방을 나간 유저 입니다."));
        joinChatRepository.delete(joinChat);

    }

    public void saveMessage(MessageRequestDto requestDto) {
        Message message = new Message(requestDto);
        messageRepository.save(message);
    }
}
