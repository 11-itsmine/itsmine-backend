package com.sparta.itsmine.domain.comment.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentAdapter {

    private final CommentRepository commentRepository;
}
