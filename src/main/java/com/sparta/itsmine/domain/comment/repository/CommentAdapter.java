package com.sparta.itsmine.domain.comment.repository;

import com.sparta.itsmine.domain.comment.entity.Comment;
import com.sparta.itsmine.global.common.ResponseExceptionEnum;
import com.sparta.itsmine.global.exception.comment.CommentAlreadyExistsException;
import com.sparta.itsmine.global.exception.comment.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentAdapter {

    private final CommentRepository commentRepository;

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
}
