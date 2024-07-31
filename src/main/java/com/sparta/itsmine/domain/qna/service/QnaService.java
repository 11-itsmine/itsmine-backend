package com.sparta.itsmine.domain.qna.service;

import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.PRODUCT_NOT_FOUND;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.QNA_NOT_FOUND;

import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.qna.dto.GetQnaResponseDto;
import com.sparta.itsmine.domain.qna.dto.QnaRequestDto;
import com.sparta.itsmine.domain.qna.entity.Qna;
import com.sparta.itsmine.domain.qna.repository.QnaRepository;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.exception.DataNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaService {

    private final QnaRepository qnaRepository;
    private final ProductRepository productRepository;


    @Transactional
    public void createQna(Long productId, QnaRequestDto requestDTO, User user) {
        Product product = getProduct(productId);
        Qna qna = Qna.of(requestDTO, user, product);

        qnaRepository.save(qna);
    }

    public Page<GetQnaResponseDto> getQnaList(Long productId, Pageable pageable, User user) {
        Product product = getProduct(productId);

        //상품의 유저 정보와 인가된 유저 정보가 같을경우, 판매자 본인
        if (product.getUser().getId().equals(user.getId())) {
            return qnaRepository.findAllByProduct(product, pageable).map(GetQnaResponseDto::of);
            //인가된 유저랑 QnA 비밀글 작성자 본인일 경우
        } else {
            Page<Qna> qnaList = qnaRepository.findAllByProductIdAndSecretQna(product.getId(), false,
                    pageable);
            Page<Qna> qnaListSecret = qnaRepository.findAllByProductIdAndUserAndSecretQna(
                    product.getId(), user.getId(), true, pageable);

            List<GetQnaResponseDto> getQnaResponseDtoList = Stream.concat(
                            qnaList.stream().map(GetQnaResponseDto::of),
                            qnaListSecret.stream().map(GetQnaResponseDto::of)
                    ).sorted((list, listSecret) -> listSecret.getUpdateAt().compareTo(list.getUpdateAt()))
                    .collect(Collectors.toList());
            return new PageImpl<>(getQnaResponseDtoList, pageable, getQnaResponseDtoList.size());
        }
    }

    public GetQnaResponseDto getQna(Long productId, Long qnaId) {
        checkProduct(productId);
        return GetQnaResponseDto.of(getQna(qnaId));
    }

    @Transactional
    public void updateQna(Long qnaId, QnaRequestDto requestDto, User user) {
        Qna qna = getQna(qnaId);
        qna.checkQnaUser(user, qna.getUser());
        qna.update(requestDto);
    }

    @Transactional
    public void deleteQna(Long productId, Long qnaId, User user) {
        checkProduct(productId);
        Qna qna = getQna(qnaId);
        qna.checkQnaUser(user, qna.getUser());
        qnaRepository.delete(qna);
    }

    /**
     * 상품 정보가 있는지 확인
     *
     * @param productId 상품 고유 번호
     */
    public void checkProduct(Long productId) {
        productRepository.findById(productId).orElseThrow(
                () -> new DataNotFoundException(PRODUCT_NOT_FOUND)
        );
    }

    /**
     * 상품 정보가 있는지 확인 후 상품 정보 반환
     *
     * @param productId 상품 고유 번호
     */
    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new DataNotFoundException(PRODUCT_NOT_FOUND)
        );
    }

    /**
     * Qna 정보를 확인 후 Entity 반환 홥니다
     *
     * @param qnaId Qna 고유 ID
     */
    public Qna getQna(Long qnaId) {
        return qnaRepository.findById(qnaId).orElseThrow(
                () -> new DataNotFoundException(QNA_NOT_FOUND)
        );
    }
}
