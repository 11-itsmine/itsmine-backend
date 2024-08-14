package com.sparta.itsmine.domain.chat.repository;

import com.sparta.itsmine.domain.chat.entity.ChatRoom;
import java.util.List;

public interface CustomChatRoomRepository {

    List<ChatRoom> findAllByFromUserIdOrToUserId(Long userId);
}
