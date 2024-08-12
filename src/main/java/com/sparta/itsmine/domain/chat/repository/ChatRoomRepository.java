package com.sparta.itsmine.domain.chat.repository;

import com.sparta.itsmine.domain.chat.entity.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>,
        CustomChatRoomRepository {

    Optional<ChatRoom> findByRoomId(String roodId);

    Optional<ChatRoom> findByFromUserIdAndToUserIdAndProductId(Long fromUserId, Long toUserId,
            Long productId);
}
