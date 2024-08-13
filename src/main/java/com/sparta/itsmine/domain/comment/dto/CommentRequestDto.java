package com.sparta.itsmine.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "저자를 입력해주세요")
    @Size(min = 1, max = 200, message = "저자의 이름은 1자 이상 200자 이하로 입력해주세요.")
    private final String author;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    @Size(min = 1, max = 200, message = "댓글은 1자 이상 200자 이하로 입력해주세요.")
    private final String content;

    @JsonCreator
    public CommentRequestDto(String author ,String content) {
        this.author = author;
        this.content = content;
    }
}
