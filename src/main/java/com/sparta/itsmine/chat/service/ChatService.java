package com.sparta.itsmine.chat.service;

import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.CHAT_ROOM_NOT_FOUND;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.CHAT_ROOM_USER_NOT_FOUND;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.USER_NOT_FOUND;

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
import com.sparta.itsmine.global.exception.DataNotFoundException;
import java.util.Comparator;
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

    /**
     * 내가 지금 참여하고 있는 채팅방 리스트를 불러 옵니다.
     *
     * @param userId 인가된 본인 유저 정보
     */
    public List<RoomInfoResponseDto> findAllRoom(Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByFromUserId(userId);
        return chatRooms.stream()
                .map(RoomInfoResponseDto::new).collect(Collectors.toList());
    }

    /**
     * 채팅방을 만듭니다..(상품의 대한 질문이나 실시간으로 확인 하고 싶을떄 1:1 방식으로 진행)
     * <p>
     * 판매자에게 채팅 요청 -> 채팅 수락 후 채팅방을 만듬 -> 만들때 유저 정보도 같이 들어감
     *
     * @param user   본인 유저 정보
     * @param userId 다른 사람의 유저 Id
     */
    public RoomInfoResponseDto createChatRoom(User user, Long userId) {
        User productUser = userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException(USER_NOT_FOUND)
        );

        ChatRoom chatRoom = new ChatRoom(user, productUser);

        log.info("chatRoom : {} ", chatRoom.getFromUser());

        //chat_room,join_chat 테이블에 동시 저장
        chatRoomRepository.save(chatRoom);
        return new RoomInfoResponseDto(chatRoom);
    }

    /**
     * 내가 선택한 채팅방에 들어 갑니다. 들어갈때 이전 채팅 메시지 내역을 불러옵니다.
     * <p>
     *
     * @param roomId 채팅방 정보
     */
    public List<Message> getMessageList(String roomId) {
        chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new DataNotFoundException(CHAT_ROOM_NOT_FOUND)
        );
        return messageRepository.findAllByRoomId(roomId).stream()
                .sorted(Comparator.comparing(Message::getTime))
                .collect(Collectors.toList());
    }

    /**
     * 채팅방에서 나갑니다. 채팅방에 나가면은 남아있는 사람은 채팅을 못합니다.
     *
     * @param user   인가된 본인 유저 정보
     * @param roomId 채팅방 정보
     */
    public void leaveUser(User user, String roomId) {
        ChatRoom chatRoom = getChatRoom(roomId);
        JoinChat joinChat = joinChatRepository.findByUserIdAndChatRoomId(user.getId(),
                        chatRoom.getId())
                .orElseThrow(() -> new DataNotFoundException(CHAT_ROOM_USER_NOT_FOUND));
        joinChatRepository.delete(joinChat);
    }

    /**
     * 메시지 저장입니다
     * <p>
     * 여기에서 mongoDB 저장을 합니다.
     */
    public void saveMessage(MessageRequestDto requestDto) {
        Message message = new Message(requestDto);
        messageRepository.save(message);
    }

    /**
     * 채팅방 정보 있는지 확인
     * <p>
     *
     * @param roomId 채팅방 ID
     */
    public ChatRoom getChatRoom(String roomId) {
        return chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new DataNotFoundException(CHAT_ROOM_NOT_FOUND)
        );
    }

//    public long getUserCount(String roomId) {
//        return joinChatRepository.
//    }
}
