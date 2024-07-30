package com.sparta.itsmine.domain.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBlackList is a Querydsl query type for BlackList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlackList extends EntityPathBase<BlackList> {

    private static final long serialVersionUID = 1452138999L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBlackList blackList = new QBlackList("blackList");

    public final QTimestamp _super = new QTimestamp(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.sparta.itsmine.domain.user.entity.QUser fromUser;

    public final NumberPath<Long> Id = createNumber("Id", Long.class);

    public final com.sparta.itsmine.domain.user.entity.QUser toUser;

    public QBlackList(String variable) {
        this(BlackList.class, forVariable(variable), INITS);
    }

    public QBlackList(Path<? extends BlackList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBlackList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBlackList(PathMetadata metadata, PathInits inits) {
        this(BlackList.class, metadata, inits);
    }

    public QBlackList(Class<? extends BlackList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fromUser = inits.isInitialized("fromUser") ? new com.sparta.itsmine.domain.user.entity.QUser(forProperty("fromUser")) : null;
        this.toUser = inits.isInitialized("toUser") ? new com.sparta.itsmine.domain.user.entity.QUser(forProperty("toUser")) : null;
    }

}

