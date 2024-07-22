package com.sparta.itsmine.domain.qna.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QnaRequestDto {

    @NotBlank(message = "제목이 비어있습니다.")
    @Size(min = 5)
    private String title;

    @NotBlank(message = "본문의 비어있습니다.")
    @Size(min = 5)
    private String content;

    private boolean secretQna = false;
}
