package com.sparta.itsmine.domain.comment.controller;

import com.sparta.itsmine.domain.comment.dto.CommentRequestDto;
import com.sparta.itsmine.domain.comment.dto.CommentResponseDto;
import com.sparta.itsmine.domain.comment.service.CommentService;
import com.sparta.itsmine.domain.security.UserDetailsImpl;
import com.sparta.itsmine.global.common.HttpResponseDto;
import com.sparta.itsmine.global.common.ResponseCodeEnum;
import com.sparta.itsmine.global.common.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/qnas/{qnaId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<HttpResponseDto> addComment(
            @PathVariable Long qnaId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CommentRequestDto commentRequestDto) {
        commentService.addComment(qnaId,userDetails.getUser(),commentRequestDto);
        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_CREATE);
    }

    // 댓글 조회
    @GetMapping
    public ResponseEntity<HttpResponseDto> getComment(
            @PathVariable Long qnaId) {

        CommentResponseDto comment = commentService.getCommentByQnaId(qnaId);
        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_GET, comment);
    }

    // 댓글 수정
    @PatchMapping
    public ResponseEntity<HttpResponseDto> updateComment(
            @PathVariable Long qnaId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.updateComment(qnaId,commentRequestDto,userDetails.getUser());
        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_UPDATE);
    }

    // 댓글 삭제
    @DeleteMapping
    public ResponseEntity<HttpResponseDto> deleteComment(
            @PathVariable Long qnaId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.deleteComment(qnaId,userDetails.getUser());
        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_DELETE);
    }
}
