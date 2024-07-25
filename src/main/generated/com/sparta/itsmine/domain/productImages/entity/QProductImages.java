package com.sparta.itsmine.domain.productImages.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductImages is a Querydsl query type for ProductImages
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductImages extends EntityPathBase<ProductImages> {

    private static final long serialVersionUID = 1722596276L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductImages productImages = new QProductImages("productImages");

    public final com.sparta.itsmine.global.common.QTimeStamp _super = new com.sparta.itsmine.global.common.QTimeStamp(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final com.sparta.itsmine.domain.product.entity.QProduct product;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.sparta.itsmine.domain.user.entity.QUser user;

    public QProductImages(String variable) {
        this(ProductImages.class, forVariable(variable), INITS);
    }

    public QProductImages(Path<? extends ProductImages> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductImages(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductImages(PathMetadata metadata, PathInits inits) {
        this(ProductImages.class, metadata, inits);
    }

    public QProductImages(Class<? extends ProductImages> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new com.sparta.itsmine.domain.product.entity.QProduct(forProperty("product"), inits.get("product")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.itsmine.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

