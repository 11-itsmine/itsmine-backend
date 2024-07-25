//package com.sparta.itsmine.domain.comment.service;
//
//import com.sparta.itsmine.domain.category.entity.Category;
//import com.sparta.itsmine.domain.comment.dto.AddCommentResponseDto;
//import com.sparta.itsmine.domain.comment.dto.CommentRequestDto;
//import com.sparta.itsmine.domain.comment.dto.CommentResponseDto;
//import com.sparta.itsmine.domain.comment.entity.Comment;
//import com.sparta.itsmine.domain.comment.repository.CommentAdapter;
//import com.sparta.itsmine.domain.comment.repository.CommentRepository;
//import com.sparta.itsmine.domain.product.entity.Product;
//import com.sparta.itsmine.domain.qna.dto.QnaRequestDto;
//import com.sparta.itsmine.domain.qna.entity.Qna;
//import com.sparta.itsmine.domain.qna.repository.QnaRepository;
//import com.sparta.itsmine.domain.user.entity.User;
//import com.sparta.itsmine.domain.user.utils.UserRole;
//import com.sparta.itsmine.global.exception.qna.QnaNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class CommentServiceTest {
//
//    @InjectMocks
//    private CommentService commentService;
//
//    @Mock
//    private CommentAdapter commentAdapter;
//
//    @Mock
//    private QnaRepository qnaRepository;
//
//    @Mock
//    private CommentRepository commentRepository;
//
//    private User testUser;
//    private Qna qna;
//    private Comment comment;
//
//    @BeforeEach
//    void setup() {
//        testUser = new User("testuser", "password", "Test", "User", "aaa@naver.com", UserRole.USER, "주소");
//        qna = new Qna(new QnaRequestDto(), testUser, new Product("상품", "상품설명", 232424, 32132, LocalDateTime.now(), new Category("전자")));
//        comment = new Comment(new CommentRequestDto("댓글"), qna);
//    }
//
//    @Test
//    @DisplayName("댓글 작성")
//    void addComment() {
//
//        // given
//        CommentRequestDto commentRequestDto = new CommentRequestDto("댓글 작성");
//        when(qnaRepository.findById(anyLong())).thenReturn(Optional.of(qna));
//        doNothing().when(commentAdapter).save(any(Comment.class));
//
//        // when
//        AddCommentResponseDto responseDto = commentService.addComment(1L, testUser, commentRequestDto);
//
//        // then
//        assertNotNull(responseDto);
//        assertEquals(responseDto.getContent(),"댓글 작성");
//        assertEquals(responseDto.getUserId(), testUser.getUsername());
//        verify(commentAdapter, times(1)).save(any(Comment.class));
//    }
//
//    @Test
//    @DisplayName("댓글 조회")
//    void getComment() {
//        // given
//        when(commentAdapter.findByQnaId(anyLong())).thenReturn(comment);
//
//        // when
//        CommentResponseDto responseDto = commentService.getComment(1L);
//
//        // then
//        assertNotNull(responseDto);
//        assertEquals(responseDto.getContent(),"댓글");
//        verify(commentAdapter, times(1)).findByQnaId(anyLong());
//    }
//
//    @Test
//    @DisplayName("댓글 수정")
//    void updateComment() {
//        // given
//        CommentRequestDto requestDto = new CommentRequestDto("댓글 내용 업데이트");
//        when(commentAdapter.findByQnaId(anyLong())).thenReturn(comment);
//
//        // when
//        commentService.updateComment(1L, requestDto, testUser);
//
//        // then
//        assertEquals(comment.getContent(), requestDto.getContent());
//        verify(commentAdapter, times(1)).findByQnaId(anyLong());
//    }
//
//    @Test
//    @DisplayName("댓글 삭제")
//    void deleteComment() {
//        // given
//        when(commentAdapter.findByQnaId(anyLong())).thenReturn(comment);
//
//        // when
//        commentService.deleteComment(1L, testUser);
//
//        // then
//        verify(commentRepository, times(1)).delete(any(Comment.class));
//    }
//
//    @Test
//    @DisplayName("QnA 가져오기")
//    void getQna() {
//        // given - when
//        when(qnaRepository.findById(anyLong())).thenReturn(Optional.of(qna));
//        Qna foundQna = commentService.getQna(1L);
//
//        // then
//        assertNotNull(foundQna);
//        assertEquals(foundQna, qna);
//        verify(qnaRepository, times(1)).findById(anyLong());
//    }
//
//    @Test
//    @DisplayName("QnA 찾지 못할 때 예외 발생")
//    void getQna_NotFound() {
//        // given - when
//        when(qnaRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        // then
//        assertThrows(QnaNotFoundException.class, () -> {
//            commentService.getQna(1L);
//        });
//        verify(qnaRepository, times(1)).findById(anyLong());
//    }
//
//    @Test
//    @DisplayName("문의사항에 이미 댓글이 있는지 확인")
//    void commentAlreadyExists() {
//        // given - when
//        commentService.commentAlreadyExists(1L);
//
//        // then
//        verify(commentAdapter, times(1)).commentAlreadyExists(anyLong());
//    }
//}
