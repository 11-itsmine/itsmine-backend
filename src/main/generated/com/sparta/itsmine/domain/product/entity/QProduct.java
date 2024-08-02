package com.sparta.itsmine.domain.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = -249291468L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final com.sparta.itsmine.global.common.QTimeStamp _super = new com.sparta.itsmine.global.common.QTimeStamp(this);

    public final ListPath<com.sparta.itsmine.domain.auction.entity.Auction, com.sparta.itsmine.domain.auction.entity.QAuction> auction = this.<com.sparta.itsmine.domain.auction.entity.Auction, com.sparta.itsmine.domain.auction.entity.QAuction>createList("auction", com.sparta.itsmine.domain.auction.entity.Auction.class, com.sparta.itsmine.domain.auction.entity.QAuction.class, PathInits.DIRECT2);

    public final NumberPath<Integer> auctionNowPrice = createNumber("auctionNowPrice", Integer.class);

    public final com.sparta.itsmine.domain.category.entity.QCategory category;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> currentPrice = createNumber("currentPrice", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final DateTimePath<java.time.LocalDateTime> dueDate = createDateTime("dueDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.sparta.itsmine.domain.images.entity.Images, com.sparta.itsmine.domain.images.entity.QImages> imagesList = this.<com.sparta.itsmine.domain.images.entity.Images, com.sparta.itsmine.domain.images.entity.QImages>createList("imagesList", com.sparta.itsmine.domain.images.entity.Images.class, com.sparta.itsmine.domain.images.entity.QImages.class, PathInits.DIRECT2);

    public final BooleanPath like = createBoolean("like");

    public final StringPath productName = createString("productName");

    public final NumberPath<Integer> startPrice = createNumber("startPrice", Integer.class);

    public final EnumPath<com.sparta.itsmine.domain.product.utils.ProductStatus> status = createEnum("status", com.sparta.itsmine.domain.product.utils.ProductStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.sparta.itsmine.domain.user.entity.QUser user;

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.sparta.itsmine.domain.category.entity.QCategory(forProperty("category")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.itsmine.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

