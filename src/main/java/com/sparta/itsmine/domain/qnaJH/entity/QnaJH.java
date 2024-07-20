package com.sparta.itsmine.domain.qnaJH.entity;

import com.sparta.itsmine.domain.comment.entity.Comment;
import com.sparta.itsmine.domain.qnaJH.dto.QnaRequestDtoJH;
import com.sparta.itsmine.global.common.TimeStamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class QnaJH extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @OneToOne(mappedBy = "qna", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Comment comment;

    public QnaJH(QnaRequestDtoJH requestDto) {
        content = requestDto.getContent();
    }
}
