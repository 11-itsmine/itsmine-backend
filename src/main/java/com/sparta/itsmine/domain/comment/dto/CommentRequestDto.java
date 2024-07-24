package com.sparta.itsmine.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    @Size(min = 1, max = 200, message = "댓글은 1자 이상 200자 이하로 입력해주세요.")
    private String content;

    @JsonCreator
    public CommentRequestDto(String content) {
        this.content = content;
    }
}
