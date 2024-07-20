package com.sparta.itsmine.domain.comment.dto;

import com.sparta.itsmine.domain.comment.entity.Comment;
import com.sparta.itsmine.domain.qnaJH.entity.QnaJH;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private final Long commentId;
    private final Long qnaId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentResponseDto(Comment comment, Long qnaId) {
        this.commentId = comment.getId();
        this.qnaId = qnaId;
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
