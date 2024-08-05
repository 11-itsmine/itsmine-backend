package com.sparta.itsmine.domain.images.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QImages is a Querydsl query type for Images
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QImages extends EntityPathBase<Images> {

    private static final long serialVersionUID = -541196578L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QImages images = new QImages("images");

    public final com.sparta.itsmine.global.common.QTimeStamp _super = new com.sparta.itsmine.global.common.QTimeStamp(this);

    public final EnumPath<com.sparta.itsmine.domain.images.util.ImageType> contentType = createEnum("contentType", com.sparta.itsmine.domain.images.util.ImageType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imagesUrl = createString("imagesUrl");

    public final com.sparta.itsmine.domain.product.entity.QProduct product;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.sparta.itsmine.domain.user.entity.QUser user;

    public QImages(String variable) {
        this(Images.class, forVariable(variable), INITS);
    }

    public QImages(Path<? extends Images> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QImages(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QImages(PathMetadata metadata, PathInits inits) {
        this(Images.class, metadata, inits);
    }

    public QImages(Class<? extends Images> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new com.sparta.itsmine.domain.product.entity.QProduct(forProperty("product"), inits.get("product")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.itsmine.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

