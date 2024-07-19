package com.sparta.itsmine.domain.comment.repository;

import com.sparta.itsmine.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
