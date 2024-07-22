package com.sparta.itsmine.domain.comment.entity;

import com.sparta.itsmine.domain.comment.dto.CommentRequestDto;
import com.sparta.itsmine.domain.qnaJH.entity.QnaJH;
import com.sparta.itsmine.global.common.TimeStamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private QnaJH qna;

    public Comment(CommentRequestDto commentRequestDto, QnaJH qna) {
        this.content = commentRequestDto.getContent();
        this.qna = qna;
    }

    public void commentUpdate(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
