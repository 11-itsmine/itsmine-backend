package com.sparta.itsmine.domain.comment.service;

import com.sparta.itsmine.domain.comment.dto.CommentRequestDto;
import com.sparta.itsmine.domain.comment.entity.Comment;
import com.sparta.itsmine.domain.comment.repository.CommentRepository;
import com.sparta.itsmine.domain.qnaJH.QnaRepositoryJH;
import com.sparta.itsmine.domain.qnaJH.entity.QnaJH;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final QnaRepositoryJH qnaRepositoryJH;
//  private final ProductRepository productRepository;

    // 댓글 작성
    public void addComment(Long qnaId, CommentRequestDto commentRequestDto) {
        // 로그인 한 사람이 판매자 인지, 상품의 유저 아이디 와 user 가 같은지 검증, User user
//        Long userId = user.getId();
//        if (!productRepository.findById(userId).equals(user.getId())) {
//            throw new IllegalArgumentException("상품의 판매자만 문의사항에 답글을 남길 수 있습니다.");
//        }

        // QnA 유무 검증
        QnaJH qna = qnaRepositoryJH.findById(qnaId).orElseThrow(
                () -> new IllegalArgumentException("QNA 가 존재하지 않습니다.")
        );

        Comment comment = new Comment(commentRequestDto,qna);

        commentRepository.save(comment);
    }
}
