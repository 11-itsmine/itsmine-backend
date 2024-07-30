package com.sparta.itsmine.domain.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJoinChat is a Querydsl query type for JoinChat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJoinChat extends EntityPathBase<JoinChat> {

    private static final long serialVersionUID = -1121289304L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJoinChat joinChat = new QJoinChat("joinChat");

    public final QTimestamp _super = new QTimestamp(this);

    public final QChatRoom chatRoom;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> Id = createNumber("Id", Long.class);

    public final com.sparta.itsmine.domain.user.entity.QUser user;

    public QJoinChat(String variable) {
        this(JoinChat.class, forVariable(variable), INITS);
    }

    public QJoinChat(Path<? extends JoinChat> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJoinChat(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJoinChat(PathMetadata metadata, PathInits inits) {
        this(JoinChat.class, metadata, inits);
    }

    public QJoinChat(Class<? extends JoinChat> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new QChatRoom(forProperty("chatRoom"), inits.get("chatRoom")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.itsmine.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

