package com.sparta.itsmine.domain.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTimestamp is a Querydsl query type for Timestamp
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QTimestamp extends EntityPathBase<Timestamp> {

    private static final long serialVersionUID = 175205840L;

    public static final QTimestamp timestamp = new QTimestamp("timestamp");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public QTimestamp(String variable) {
        super(Timestamp.class, forVariable(variable));
    }

    public QTimestamp(Path<? extends Timestamp> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTimestamp(PathMetadata metadata) {
        super(Timestamp.class, metadata);
    }

}

