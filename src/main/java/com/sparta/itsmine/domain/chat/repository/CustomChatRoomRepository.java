package com.sparta.itsmine.domain.chat.repository;

import com.sparta.itsmine.domain.chat.entity.ChatRoom;
import java.util.List;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

public interface CustomChatRoomRepository {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
    List<ChatRoom> findAllByFromUserIdOrToUserId(Long userId);
}
