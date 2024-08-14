package com.sparta.itsmine.domain.chat.repository;

import com.sparta.itsmine.domain.chat.entity.ChatRoom;
import java.util.List;

import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

public interface CustomChatRoomRepository {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ChatRoom> findAllByFromUserIdOrToUserId(Long userId);
}
