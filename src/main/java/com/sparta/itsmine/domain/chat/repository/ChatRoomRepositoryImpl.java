package com.sparta.itsmine.domain.chat.repository;

import static com.sparta.itsmine.domain.chat.entity.QChatRoom.chatRoom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.itsmine.domain.chat.entity.ChatRoom;
import com.sparta.itsmine.domain.chat.entity.ChatStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements CustomChatRoomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Cacheable(value = "chatRooms", key = "#userId")
    public List<ChatRoom> findAllByFromUserIdOrToUserId(Long userId) {

        return jpaQueryFactory.selectFrom(chatRoom)
                .where(chatRoom.fromUser.id.eq(userId)
                        .and(chatRoom.fromUserStatus.in(ChatStatus.TALK, ChatStatus.BLOCK))
                        .or(chatRoom.toUser.id.eq(userId)
                                .and(chatRoom.toUserStatus.in(ChatStatus.TALK, ChatStatus.BLOCK))))
                .fetch();
    }
}
