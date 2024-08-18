package com.sparta.itsmine.domain.comment.repository;

import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.COMMENT_ALREADY_EXISTS;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.COMMENT_NOT_FOUND;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.NO_AUTHORIZATION_COMMNET;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.QNA_NOT_FOUND;

import com.sparta.itsmine.domain.comment.entity.Comment;
import com.sparta.itsmine.domain.qna.entity.Qna;
import com.sparta.itsmine.domain.qna.repository.QnaRepository;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.domain.user.utils.UserRole;
import com.sparta.itsmine.global.exception.DataDuplicatedException;
import com.sparta.itsmine.global.exception.DataNotFoundException;
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
            throw new DataNotFoundException(COMMENT_NOT_FOUND);
        }
        return comment;
    }

    public Qna getQna(Long qnaId) {
        return qnaRepository.findById(qnaId).orElseThrow(
                () -> new DataNotFoundException(QNA_NOT_FOUND)
        );
    }

    public void checkDuplicateComment(Long qnaId) {
        if (commentRepository.findByQnaId(qnaId) != null) {
            throw new DataDuplicatedException(COMMENT_ALREADY_EXISTS);
        }
    }

    public void checkUserAndManager(Qna qna, User user){
        // 판매자 또는 MANAGER만 댓글 작성 가능
        if (!qna.getProduct().getUser().getId().equals(user.getId()) && !user.getUserRole().equals(
                UserRole.MANAGER)) {
            throw new DataNotFoundException(NO_AUTHORIZATION_COMMNET);
        }
    }
}
