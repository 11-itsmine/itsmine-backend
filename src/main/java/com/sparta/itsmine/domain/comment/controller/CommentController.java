package com.sparta.itsmine.domain.comment.controller;

import com.sparta.itsmine.domain.comment.dto.CommentRequestDto;
import com.sparta.itsmine.domain.comment.dto.CommentResponseDto;
import com.sparta.itsmine.domain.comment.service.CommentService;
import com.sparta.itsmine.global.common.HttpResponseDto;
import com.sparta.itsmine.global.common.ResponseCodeEnum;
import com.sparta.itsmine.global.common.ResponseUtils;
import com.sparta.itsmine.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping("/qnas/{qnaId}/comments")
    public ResponseEntity<HttpResponseDto> addComment(
            @PathVariable Long qnaId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CommentRequestDto commentRequestDto) {
        commentService.addComment(qnaId,userDetails.getUser(),commentRequestDto);
        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_CREATE);
    }

    // 댓글 조회
    @GetMapping("/qnas/{qnaId}/comments")
    public ResponseEntity<HttpResponseDto> getComment(
            @PathVariable Long qnaId) {

        CommentResponseDto comment = commentService.getCommentByQnaId(qnaId);
        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_GET, comment);
    }

    // 댓글 수정
    @PatchMapping("/qnas/{qnaId}/comments/{commentId}")
    public ResponseEntity<HttpResponseDto> updateComment(
            @PathVariable Long qnaId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.updateComment(qnaId,commentId,commentRequestDto,userDetails.getUser());
        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_UPDATE);
    }
}
