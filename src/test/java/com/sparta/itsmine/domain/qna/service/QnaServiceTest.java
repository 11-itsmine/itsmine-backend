package com.sparta.itsmine.domain.qna.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sparta.itsmine.domain.category.entity.Category;
import com.sparta.itsmine.domain.category.repository.CategoryRepository;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.qna.dto.QnaRequestDto;
import com.sparta.itsmine.domain.qna.repository.QnaRepository;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.domain.user.repository.UserRepository;
import com.sparta.itsmine.domain.user.utils.UserRole;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class QnaServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QnaService qnaService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private QnaRepository qnaRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product product;
    private Category category;
    private User user;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void initDate() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

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

        userRepository.save(user);
        categoryRepository.save(category);
        productRepository.save(product);

    }

    @Test
    @DisplayName("생성 테스트")
    public void testCreateQna() throws Exception {
        QnaRequestDto requestDto = new QnaRequestDto();
        requestDto.setContent("Test QnA");
        requestDto.setSecretQna(false);
        requestDto.setTitle("Test Title");

        ResultActions actions = mockMvc.perform(
                get("products/{productId}/qnas", 1L, requestDto)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test QnA"))
                .andExpect(jsonPath("$.content").value("Test Content"))
                .andDo(print());
    }
}