package com.sparta.itsmine.domain.comment.repository;

import com.sparta.itsmine.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByQnaId(Long qnaId);
}
