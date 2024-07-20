package com.sparta.itsmine.domain.qna.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QnaRequestDto {

    @NotBlank(message = "제목이 비어있습니다.")
    private String title;

    @NotBlank(message = "본문의 비어있습니다.")
    private String content;

    private boolean secretQna = false;
}
