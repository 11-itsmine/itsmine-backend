package com.sparta.itsmine.domain.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = 1718311001L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final com.sparta.itsmine.domain.user.entity.QUser fromUser;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<JoinChat, QJoinChat> joinChats = this.<JoinChat, QJoinChat>createList("joinChats", JoinChat.class, QJoinChat.class, PathInits.DIRECT2);

    public final StringPath roomId = createString("roomId");

    public final com.sparta.itsmine.domain.user.entity.QUser toUser;

    public QChatRoom(String variable) {
        this(ChatRoom.class, forVariable(variable), INITS);
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoom(PathMetadata metadata, PathInits inits) {
        this(ChatRoom.class, metadata, inits);
    }

    public QChatRoom(Class<? extends ChatRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fromUser = inits.isInitialized("fromUser") ? new com.sparta.itsmine.domain.user.entity.QUser(forProperty("fromUser")) : null;
        this.toUser = inits.isInitialized("toUser") ? new com.sparta.itsmine.domain.user.entity.QUser(forProperty("toUser")) : null;
    }

}

