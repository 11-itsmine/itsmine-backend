//package com.sparta.itsmine.domain.qna.entity;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.sparta.itsmine.domain.product.entity.Product;
//import com.sparta.itsmine.domain.qna.dto.QnaRequestDto;
//import com.sparta.itsmine.domain.user.entity.User;
//import com.sparta.itsmine.domain.user.utils.UserRole;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//class QnaTest {
//
//    User user;
//    Product product;
//
//    @BeforeEach
//    void initDate() {      // 2
//        User.builder()
//                .username("TsetUser")
//                .encodedPassword("tset12345!!")
//                .name("이순머")
//                .nickname("모모")
//                .role(UserRole.USER)
//                .address("충남 어딘가")
//                .email("fltnsah@nate.com")
//                .build();
//        product = new Product(1L, "마우스", "마우스 팝니다", 50000, 1000);
//    }
//
//    @Test
//    @DisplayName("Qna 생성 테스트")
//    void updateTest() {
//        QnaRequestDto requestDto = new QnaRequestDto();
//        requestDto.setTitle("제목");
//        requestDto.setContent("내용");
//        requestDto.setSecretQna(false);
//        Qna qna = Qna.of(requestDto, user, product);
//
//        //when
//        requestDto.setTitle("수정-제목");
//        requestDto.setContent("수정-내용");
//        requestDto.setSecretQna(true);
//
//        qna.update(requestDto);
//
//        //then
//        assertThat(qna.getTitle()).isEqualTo("수정-제목");
//    }
//}