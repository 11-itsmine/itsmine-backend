package com.sparta.itsmine.domain.qna.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.qna.dto.QnaRequestDto;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.domain.user.utils.UserRole;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QnaTest {

    User user;
    Category category;
    Product product;


    @BeforeEach
    void initDate() {      // 2
        User.builder()
                .username("TsetUser")
                .encodedPassword("tset12345!!")
                .name("이순머")
                .nickname("모모")
                .role(UserRole.USER)
                .address("충남 어딘가")
                .email("fltnsah@nate.com")
                .build();
        Category.builder().categoryName("전자").build();
        product = new Product("마우스", "마우스ㅍㅍ", 5000
                , 50000, LocalDateTime.parse("2024-07-23 02:21:48.628888")
                , category);
    }

    @Test
    @DisplayName("Qna 생성 테스트")
    void updateTest() {
        QnaRequestDto requestDto = new QnaRequestDto();
        requestDto.setTitle("제목");
        requestDto.setContent("내용");
        requestDto.setSecretQna(false);
        Qna qna = Qna.of(requestDto, user, product);

        //when
        requestDto.setTitle("수정-제목");
        requestDto.setContent("수정-내용");
        requestDto.setSecretQna(true);

        qna.update(requestDto);

        //then
        assertThat(qna.getTitle()).isEqualTo("수정-제목");
    }
}