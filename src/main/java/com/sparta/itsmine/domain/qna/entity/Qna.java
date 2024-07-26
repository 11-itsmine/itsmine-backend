package com.sparta.itsmine.domain.qna.entity;

import com.sparta.itsmine.domain.comment.entity.Comment;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.qna.dto.QnaRequestDto;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.TimeStamp;
import com.sparta.itsmine.global.exception.comment.CommentEqualSellerException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.COMMENT_EQUAL_SELLER;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(mappedBy = "qna", cascade = CascadeType.ALL, orphanRemoval = true)
    private Comment comment;

    public Qna(QnaRequestDto requestDto, User user, Product product) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user;
        this.product = product;
        this.secretQna = requestDto.isSecretQna();
    }

    public static Qna of(QnaRequestDto qnaRequestDTO, User user, Product product) {
        return new Qna(qnaRequestDTO, user, product);
    }

    public void update(QnaRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.secretQna = requestDto.isSecretQna();
    }

    // 판매자 확인
    public void equalsSeller(Long userId) {
        if (!this.product.getUser().getId().equals(userId)) {
            throw new CommentEqualSellerException(COMMENT_EQUAL_SELLER);
        }
    }
}
