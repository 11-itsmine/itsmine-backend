package com.sparta.itsmine.domain.comment.repository;

import com.sparta.itsmine.domain.comment.entity.Comment;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.qna.entity.Qna;
import com.sparta.itsmine.domain.qna.repository.QnaRepository;
import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;
import com.sparta.itsmine.global.exception.comment.CommentAlreadyExistsException;
import com.sparta.itsmine.global.exception.comment.CommentEqualSellerException;
import com.sparta.itsmine.global.exception.comment.CommentNotFoundException;
import com.sparta.itsmine.global.exception.qna.QnaNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentAdapter {

    private final CommentRepository commentRepository;
    private final QnaRepository qnaRepository;

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public Comment findByQnaId(Long qnaId) {
        Comment comment = commentRepository.findByQnaId(qnaId);
        if (comment == null) {
            throw new CommentNotFoundException(ResponseExceptionEnum.COMMENT_NOT_FOUND);
        }
        return comment;
    }

    public void commentAlreadyExists(Long qnaId) {
        if (commentRepository.findByQnaId(qnaId) != null) {
            throw new CommentAlreadyExistsException(ResponseExceptionEnum.COMMENT_ALREADY_EXISTS);
        }
    }

    public void equalsSeller(Long qnaId, Long userId) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new QnaNotFoundException(ResponseExceptionEnum.QNA_NOT_FOUND));
        Product product = qna.getProduct();

        if (!product.getUser().getId().equals(userId)) {
            throw new CommentEqualSellerException(ResponseExceptionEnum.COMMENT_EQUAL_SELLER);
        }
    }
}
