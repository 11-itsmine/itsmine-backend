package com.sparta.itsmine.domain.qna.entity;

import com.sparta.itsmine.domain.comment.entity.Comment;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.qna.dto.QnaRequestDto;
import com.sparta.itsmine.global.common.TimeStamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qna extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "qna_id")
    private Long id;

    private String title;

    private String content;

    private boolean secretQna;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @OneToMany(mappedBy = "qna", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    private Qna(QnaRequestDto requestDto, Product product) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.product = product;
        this.secretQna = requestDto.isSecretQna();
    }

    public static Qna of(QnaRequestDto qnaRequestDTO, Product product) {
        return new Qna(qnaRequestDTO, product);
    }

    public void update(QnaRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.secretQna = requestDto.isSecretQna();
    }
}
