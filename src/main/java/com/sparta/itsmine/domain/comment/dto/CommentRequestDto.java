package com.sparta.itsmine.domain.comment.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

}
