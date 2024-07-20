package com.sparta.itsmine.domain.qna.dto;

import com.sparta.itsmine.domain.qna.entity.Qna;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class GetQnaResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final boolean secretQna;
    private final String username;
    private final String nickname;
    private final LocalDateTime createAt;
    private final LocalDateTime updateAt;

    private GetQnaResponseDto(Qna qna) {
        this.id = qna.getId();
        this.title = qna.getTitle();
        this.content = qna.getContent();
        this.secretQna = qna.isSecretQna();
        this.username = qna.getUser().getUsername();
        this.nickname = qna.getUser().getNickname();
        this.createAt = qna.getCreatedAt();
        this.updateAt = qna.getUpdatedAt();
    }

    public static GetQnaResponseDto of(Qna qna) {
        return new GetQnaResponseDto(qna);
    }

}
