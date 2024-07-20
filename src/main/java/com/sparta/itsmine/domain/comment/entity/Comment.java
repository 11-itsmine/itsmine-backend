package com.sparta.itsmine.domain.comment.entity;

import com.sparta.itsmine.domain.comment.dto.CommentRequestDto;
import com.sparta.itsmine.domain.qnaJH.entity.QnaJH;
import com.sparta.itsmine.global.common.TimeStamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @OneToOne
    @JoinColumn(name = "qna_id")
    private QnaJH qna;

    public Comment(CommentRequestDto commentRequestDto, QnaJH qna) {
        this.content = commentRequestDto.getContent();
        this.qna = qna;
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
