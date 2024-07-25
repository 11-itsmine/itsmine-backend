//package com.sparta.itsmine.domain.comment.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sparta.itsmine.domain.category.entity.Category;
//import com.sparta.itsmine.domain.comment.dto.AddCommentResponseDto;
//import com.sparta.itsmine.domain.comment.dto.CommentRequestDto;
//import com.sparta.itsmine.domain.comment.dto.CommentResponseDto;
//import com.sparta.itsmine.domain.comment.entity.Comment;
//import com.sparta.itsmine.domain.comment.service.CommentService;
//import com.sparta.itsmine.domain.product.entity.Product;
//import com.sparta.itsmine.domain.qna.dto.QnaRequestDto;
//import com.sparta.itsmine.domain.qna.entity.Qna;
//import com.sparta.itsmine.domain.user.entity.User;
//import com.sparta.itsmine.domain.user.utils.UserRole;
//import com.sparta.itsmine.global.common.config.WebSecurityConfig;
//import com.sparta.itsmine.global.security.MockSpringSecurityFilter;
//import com.sparta.itsmine.global.security.UserDetailsImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.security.Principal;
//import java.time.LocalDateTime;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(
//        controllers = {CommentController.class},
//        excludeFilters = {
//                @ComponentScan.Filter(
//                        type = FilterType.ASSIGNABLE_TYPE,
//                        classes = WebSecurityConfig.class
//                )
//        }
//)
//public class CommentControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    private Principal mockPrincipal;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private CommentService commentService;
//
//    private Comment comment;
//
//    private User testUser;
//
//    @BeforeEach
//    public void setup() {
//        testUser = new User(
//                "testuser",
//                "password",
//                "Test", "User",
//                "aaa@naver.com",
//                UserRole.USER,
//                "주소");
//
//        comment = new Comment(new CommentRequestDto("댓글"), new Qna(new QnaRequestDto(), testUser, new Product("상품","상품설명",232424,32132, LocalDateTime.now(),new Category("전자"))));
//
//        UserDetailsImpl userDetails = new UserDetailsImpl(testUser);
//        mockPrincipal = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//
//        mvc = MockMvcBuilders.webAppContextSetup(context)
//                .apply(springSecurity(new MockSpringSecurityFilter()))
//                .alwaysDo(print())
//                .build();
//    }
//
//    @Test
//    @DisplayName("댓글 작성")
//    void addComment() throws Exception {
//        // given
//        Long qnaId = 1L;
//        String content = "댓글 작성";
//        CommentRequestDto commentRequestDto = new CommentRequestDto(content);
//        String addCommentJson = objectMapper.writeValueAsString(commentRequestDto);
//        AddCommentResponseDto addCommentResponseDto = new AddCommentResponseDto(comment,testUser);
//        when(commentService.addComment(eq(qnaId), any(User.class), eq(commentRequestDto)))
//                .thenReturn(addCommentResponseDto);
//
//        // when - then
//        mvc.perform(post("/qnas/{qnaId}/comments", qnaId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(addCommentJson)
//                        .principal(mockPrincipal))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("댓글 작성이 완료 되었습니다."))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("댓글 조회")
//    void getComment() throws Exception {
//        // given
//        Long qnaId = 1L;
//        CommentResponseDto commentResponseDto = new CommentResponseDto(comment, qnaId);
//        when(commentService.getComment(eq(qnaId))).thenReturn(commentResponseDto);
//
//        // when - then
//        mvc.perform(get("/qnas/{qnaId}/comments", qnaId)
//                        .principal(mockPrincipal))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("댓글 조회가 완료 되었습니다."))
//                .andExpect(jsonPath("$.data").exists())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("댓글 수정")
//    void updateComment() throws Exception {
//        // given
//        Long qnaId = 1L;
//        String content = "댓글 내용 업데이트";
//        CommentRequestDto commentRequestDto = new CommentRequestDto(content);
//        String updateCommentJson = objectMapper.writeValueAsString(commentRequestDto);
//
//        // when - then
//        mvc.perform(patch("/qnas/{qnaId}/comments", qnaId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(updateCommentJson)
//                        .principal(mockPrincipal))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("댓글 수정이 완료 되었습니다."))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("댓글 삭제")
//    void deleteComment() throws Exception {
//        // given
//        Long qnaId = 1L;
//
//        // when - then
//        mvc.perform(delete("/qnas/{qnaId}/comments", qnaId)
//                        .principal(mockPrincipal))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("댓글 삭제가 완료 되었습니다."))
//                .andDo(print());
//    }
//}
//
