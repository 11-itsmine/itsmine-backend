package com.sparta.itsmine.domain.qna.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class QnAChangeRequestDto {

    @NotBlank(message = "제목이 비어있습니다.")
    @Size(min = 3)
    private String title;

    @NotBlank(message = "본문의 비어있습니다.")
    @Size(min = 3)
    private String content;

    @NotBlank(message = "패스워드를 입력해주세요.")
    private String password;

    private boolean secretQna;

}
