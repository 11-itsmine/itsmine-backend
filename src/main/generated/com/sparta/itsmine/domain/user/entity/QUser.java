package com.sparta.itsmine.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -93233468L;

    public static final QUser user = new QUser("user");

    public final com.sparta.itsmine.global.common.QTimeStamp _super = new com.sparta.itsmine.global.common.QTimeStamp(this);

    public final StringPath address = createString("address");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.sparta.itsmine.domain.images.entity.Images, com.sparta.itsmine.domain.images.entity.QImages> imagesList = this.<com.sparta.itsmine.domain.images.entity.Images, com.sparta.itsmine.domain.images.entity.QImages>createList("imagesList", com.sparta.itsmine.domain.images.entity.Images.class, com.sparta.itsmine.domain.images.entity.QImages.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath username = createString("username");

    public final EnumPath<com.sparta.itsmine.domain.user.utils.UserRole> userRole = createEnum("userRole", com.sparta.itsmine.domain.user.utils.UserRole.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

