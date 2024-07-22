package com.sparta.itsmine.domain.comment.service;

import com.sparta.itsmine.domain.comment.dto.AddCommentResponseDto;
import com.sparta.itsmine.domain.comment.dto.CommentRequestDto;
import com.sparta.itsmine.domain.comment.dto.CommentResponseDto;
import com.sparta.itsmine.domain.comment.entity.Comment;
import com.sparta.itsmine.domain.comment.repository.CommentAdapter;
import com.sparta.itsmine.domain.comment.repository.CommentRepository;
import com.sparta.itsmine.domain.qnaJH.QnaRepositoryJH;
import com.sparta.itsmine.domain.qnaJH.entity.QnaJH;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.ResponseExceptionEnum;
import com.sparta.itsmine.global.exception.comment.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentAdapter commentAdapter;
    private final QnaRepository qnaRepository;
    private final CommentRepository commentRepository;
//  private final ProductRepository productRepository;

    // 댓글 작성
    @Transactional
    public AddCommentResponseDto addComment(Long qnaId, User user, CommentRequestDto commentRequestDto) {

//         판매자만 댓글 작성 가능
//        equalsSeller(user.getId());

        Qna qna = getQna(qnaId);

        commentAlreadyExists(qnaId);

        Comment comment = new Comment(commentRequestDto, qna);

        commentAdapter.save(comment);

        return new AddCommentResponseDto(comment,user);
    }

    // 댓글 조회
    public CommentResponseDto getComment(Long qnaId) {

        Comment comment = commentAdapter.findByQnaId(qnaId);

        return new CommentResponseDto(comment, qnaId);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long qnaId, CommentRequestDto requestDto, User user) {
//         판매자만 댓글 수정 가능
//        equalsSeller(user.getId());

        // 댓글 가져오기
        Comment comment = getCommentByQnaId(qnaId);

        comment.commentUpdate(requestDto);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long qnaId, User user) {

        // 판매자만 댓글 삭제 가능
//        equalsSeller(user.getId());

        Comment comment = getCommentByQnaId(qnaId);

        commentRepository.delete(comment);
    }

    // 판매자 확인
//    public void equalsSeller(Long userId) {
//        if (!productRepository.findByUserId(userId).equals(userId)) {
//            throw new CommentEqualSellerException(ResponseExceptionEnum.COMMENT_EQUAL_SELLER);
//        }
//    }

    // QnA 가져오기
    public Qna getQna(Long qnaId) {
        return qnaRepository.findById(qnaId).orElseThrow(
                () -> new QnaNotFoundExceptionJH(ResponseExceptionEnum.QNAJH_NOT_FOUND)
        );
    }

    // 댓글 가져오기
    public Comment getCommentByQnaId(Long qnaId) {
        return commentAdapter.findByQnaId(qnaId);
    }

    // 문의사항에 이미 댓글이 있는지 확인
    private void commentAlreadyExists(Long qnaId) {
        commentAdapter.commentAlreadyExists(qnaId);
    }
}
