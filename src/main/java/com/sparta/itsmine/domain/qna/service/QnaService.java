package com.sparta.itsmine.domain.qna.service;

import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.qna.dto.CreateQnaRequestDTO;
import com.sparta.itsmine.domain.qna.entity.Qna;
import com.sparta.itsmine.domain.qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaService {

    private final QnaRepository qnaRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void createQna(Long productId, CreateQnaRequestDTO requestDTO) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("상품 정보가 없습니다.")
        );
        Qna qna = Qna.of(requestDTO, product);

        qnaRepository.save(qna);
    }
}
