package com.sparta.itsmine.domain.comment.dto;

import com.sparta.itsmine.domain.comment.entity.Comment;
import com.sparta.itsmine.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AddCommentResponseDto {

    private final String userId; //댓글 작성한 유저 로그인 ID
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public AddCommentResponseDto(Comment comment, User user) {
        this.userId = user.getUsername();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
