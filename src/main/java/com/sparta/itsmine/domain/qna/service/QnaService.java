package com.sparta.itsmine.domain.qna.service;

import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.qna.dto.QnaRequestDto;
import com.sparta.itsmine.domain.qna.entity.Qna;
import com.sparta.itsmine.domain.qna.repository.QnaRepository;
import com.sparta.itsmine.domain.security.UserDetailsImpl;
import com.sparta.itsmine.domain.user.entity.User;
import java.util.List;
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
    public void createQna(Long productId, QnaRequestDto requestDTO, User user) {
        Product product = getProductEntity(productId);
        Qna qna = Qna.of(requestDTO, product);

        qnaRepository.save(qna);
    }

    public List<Qna> getQnaList(Long productId) {
        Product product = getProductEntity(productId);
        return qnaRepository.findAllByProduct(product);
    }

    public Qna getQna(Long productId, Long qnaId) {
        Product product = getProductEntity(productId);

        return qnaRepository.findByIdAndAndProduct(qnaId, product).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 문의 입니다")
        );
    }

    @Transactional
    public void updateQna(Long productId, Long qnaId, QnaRequestDto requestDto, User user) {
        Product product = getProductEntity(productId);
        Qna qna = qnaRepository.findByIdAndAndProduct(qnaId, product).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 문의 입니다")
        );
        qna.update(requestDto);
    }

    @Transactional
    public void deleteQna(Long productId, Long qnaId, UserDetailsImpl userDetails) {
        Product product = getProductEntity(productId);
        Qna qna = qnaRepository.findByIdAndAndProduct(qnaId, product).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 문의 입니다")
        );
        qnaRepository.delete(qna);
    }


    public Product getProductEntity(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("상품 정보가 없습니다.")
        );
    }
}
