package com.sparta.itsmine.domain.chat.repository;

import static com.sparta.itsmine.domain.chat.entity.QChatRoom.chatRoom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.itsmine.domain.chat.entity.ChatRoom;
import com.sparta.itsmine.domain.chat.entity.ChatStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements CustomChatRoomRepository{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<ChatRoom> findAllByFromUserIdOrToUserId(Long userId) {

        return jpaQueryFactory.selectFrom(chatRoom)
                .where(chatRoom.fromUser.id.eq(userId)
                        .and(chatRoom.fromUserStatus.eq(ChatStatus.TALK))
                        .or(chatRoom.toUser.id.eq(userId)
                                .and(chatRoom.toUserStatus.eq(ChatStatus.TALK))))
                .fetch();
    }
}
