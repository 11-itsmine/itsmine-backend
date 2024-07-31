package com.sparta.itsmine.domain.chat.service;

import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.CHAT_BLACKLIST_USER;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.CHAT_NOT_ONE_TO_ONE;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.CHAT_ROOM_NOT_FOUND;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.CHAT_ROOM_USER_NOT_FOUND;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.USER_NOT_FOUND;

import com.sparta.itsmine.domain.chat.dto.MessageRequestDto;
import com.sparta.itsmine.domain.chat.dto.RoomInfoResponseDto;
import com.sparta.itsmine.domain.chat.entity.BlackList;
import com.sparta.itsmine.domain.chat.entity.ChatRoom;
import com.sparta.itsmine.domain.chat.entity.JoinChat;
import com.sparta.itsmine.domain.chat.entity.Message;
import com.sparta.itsmine.domain.chat.repository.BlackListRepository;
import com.sparta.itsmine.domain.chat.repository.ChatRoomRepository;
import com.sparta.itsmine.domain.chat.repository.JoinChatRepository;
import com.sparta.itsmine.domain.chat.repository.MessageRepository;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.domain.user.repository.UserRepository;
import com.sparta.itsmine.global.exception.DataDuplicatedException;
import com.sparta.itsmine.global.exception.DataNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final JoinChatRepository joinChatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final BlackListRepository blackListRepository;

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
     * @param fromUser 본인 유저 정보
     * @param userId   다른 사람의 유저 Id
     */
    public RoomInfoResponseDto createChatRoom(User fromUser, Long userId) {
        Optional<BlackList> blackList = blackListRepository.findByFromUserIdAndToUserId(
                fromUser.getId(),
                userId);
        User toUser = getUser(userId);
        if (blackList.isPresent()) {
            throw new DataDuplicatedException(CHAT_BLACKLIST_USER);
        }

        ChatRoom chatRoom = new ChatRoom(fromUser, toUser);

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
     * 블랙리스트를 추가 합니다
     * <p>
     * (채팅을 하다가 블랙 리스트를 추가하여 마음에 들지 않는 유저의 채팅을 받지 못하게 한다)
     *
     * @param userId 유저 ID
     */
    @Transactional
    public boolean isBlackList(User fromuser, Long userId) {

        Optional<BlackList> CheckBlackList = blackListRepository.findByFromUserIdAndToUserId(
                fromuser.getId(),
                userId);

        if (CheckBlackList.isEmpty()) {
            User toUser = getUser(userId);
            BlackList blackList = new BlackList(fromuser, toUser);
            blackListRepository.save(blackList);
            return true;
        } else {
            blackListRepository.delete(CheckBlackList.get());
            return false;
        }
    }

    /**
     * 유저 정보 있는지 확인
     * <p>
     *
     * @param userId 유저 ID
     */
    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException(USER_NOT_FOUND)
        );
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

    /**
     * 채팅방 유저가 2명 이하면 채팅 이용 불가 예외 처리
     * <p>
     *
     * @param roomId 채팅방 ID
     */
    public void getUserCount(String roomId) {
        long userCount = joinChatRepository.countByChatRoomId(roomId);
        if (userCount < 2) {
            throw new DataNotFoundException(CHAT_NOT_ONE_TO_ONE);
        }
    }

}
