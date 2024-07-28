package com.sparta.itsmine.chat.repository;

import com.sparta.itsmine.chat.entity.JoinChat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinChatRepository extends JpaRepository<JoinChat, Long> {

    Optional<JoinChat> findByUserIdAndChatRoomId(Long userId, Long chatRoomId);
}
