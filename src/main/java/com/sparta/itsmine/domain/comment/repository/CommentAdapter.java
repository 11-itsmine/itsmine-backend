package com.sparta.itsmine.domain.comment.repository;

import com.sparta.itsmine.domain.comment.entity.Comment;
import com.sparta.itsmine.domain.qna.entity.Qna;
import com.sparta.itsmine.domain.qna.repository.QnaRepository;
import com.sparta.itsmine.global.exception.comment.CommentAlreadyExistsException;
import com.sparta.itsmine.global.exception.comment.CommentNotFoundException;
import com.sparta.itsmine.global.exception.qna.QnaNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.*;

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
            throw new CommentNotFoundException(COMMENT_NOT_FOUND);
        }
        return comment;
    }

    public Qna getQna(Long qnaId) {
        return qnaRepository.findById(qnaId).orElseThrow(
                () -> new QnaNotFoundException(QNA_NOT_FOUND)
        );
    }

    public void checkDuplicateComment(Long qnaId) {
        if (commentRepository.findByQnaId(qnaId) != null) {
            throw new CommentAlreadyExistsException(COMMENT_ALREADY_EXISTS);
        }
    }
}
