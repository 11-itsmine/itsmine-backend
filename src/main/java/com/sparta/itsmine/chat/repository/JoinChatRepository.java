package com.sparta.itsmine.chat.repository;

import com.sparta.itsmine.chat.entity.JoinChat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JoinChatRepository extends JpaRepository<JoinChat, Long> {

    Optional<JoinChat> findByUserIdAndChatRoomId(Long userId, Long chatRoomId);

    @Query("SELECT COUNT(jc) FROM JoinChat jc WHERE jc.chatRoom.roomId = :roomId")
    long countByChatRoomId(@Param("roomId") String roomId);

}
