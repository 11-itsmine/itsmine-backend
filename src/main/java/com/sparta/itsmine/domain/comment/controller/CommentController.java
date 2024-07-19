package com.sparta.itsmine.domain.comment.controller;

import com.sparta.itsmine.domain.comment.dto.CommentRequestDto;
import com.sparta.itsmine.domain.comment.service.CommentService;
import com.sparta.itsmine.global.common.HttpResponseDto;
import com.sparta.itsmine.global.common.ResponseCodeEnum;
import com.sparta.itsmine.global.common.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/qnas/{qnaId}/comments")
    public ResponseEntity<HttpResponseDto> addComment(
            @PathVariable Long qnaId,
//          @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CommentRequestDto commentRequestDto) {

        commentService.addComment(qnaId,commentRequestDto); // userDetails.getUser()
        return ResponseUtils.of(ResponseCodeEnum.COMMENT_SUCCESS_CREATE);
    }
}
