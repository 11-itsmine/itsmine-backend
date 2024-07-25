package com.sparta.itsmine.chat.service;

import com.sparta.itsmine.chat.dto.ChatMessageDto;
import com.sparta.itsmine.chat.entity.ChatRoom;
import com.sparta.itsmine.chat.entity.JoinChat;
import com.sparta.itsmine.chat.entity.Message;
import com.sparta.itsmine.chat.repository.ChatRoomAdapter;
import com.sparta.itsmine.chat.repository.JoinChatAdapter;
import com.sparta.itsmine.chat.repository.MessageAdapter;
import com.sparta.itsmine.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomAdapter chatRoomAdapter;
    private final JoinChatAdapter joinChatAdapter;
    private final MessageAdapter messageAdapter;

    public List<ChatRoom> findAllRoom() {
        return chatRoomAdapter.findAll();
    }

    public ChatRoom findRoomById(Long id) {
        return chatRoomAdapter.findById(id);
    }

    public ChatRoom createRoom() {
        return chatRoomAdapter.save(ChatRoom.createRoom());
    }

    public Message createMessage(Long roomId, ChatMessageDto message, User user) {
        ChatRoom room = chatRoomAdapter.findById(roomId); //방 찾기
        JoinChat joinChat = joinChatAdapter.findByUserIdAndChatRoomId(user.getId(),
                roomId); //참여된 채팅 찾기
        return messageAdapter.save(Message.createMessage(room, user, joinChat));
    }

    // 채팅방 채팅내용 불러오기
    public List<Message> findAllByJoinChatId(Long joinChatId) {
        return messageAdapter.findAllByJoinChatId(joinChatId);
    }
}
