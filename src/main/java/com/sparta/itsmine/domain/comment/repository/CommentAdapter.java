package com.sparta.itsmine.domain.comment.repository;

import com.sparta.itsmine.global.common.ResponseExceptionEnum;
import com.sparta.itsmine.global.exception.comment.CommentAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentAdapter {

    private final CommentRepository commentRepository;

    public void commentExists(Long qnaId) {
        if (commentRepository.findByQnaId(qnaId) != null) {
            throw new CommentAlreadyExistsException(ResponseExceptionEnum.COMMENT_ALREADY_EXISTS);
        }
    }
}
