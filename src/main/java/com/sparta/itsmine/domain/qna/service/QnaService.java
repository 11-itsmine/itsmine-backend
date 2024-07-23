package com.sparta.itsmine.domain.qna.service;

import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.qna.dto.GetQnaResponseDto;
import com.sparta.itsmine.domain.qna.dto.QnaRequestDto;
import com.sparta.itsmine.domain.qna.entity.Qna;
import com.sparta.itsmine.domain.qna.repository.QnaRepository;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.common.ResponseExceptionEnum;
import com.sparta.itsmine.global.exception.qna.QnaCheckUserException;
import com.sparta.itsmine.global.exception.qna.QnaNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaService {

    private static final Logger log = LoggerFactory.getLogger(QnaService.class);
    private final QnaRepository qnaRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void createQna(Long productId, QnaRequestDto requestDTO, User user) {
        Product product = getProductEntity(productId);
        Qna qna = Qna.of(requestDTO, user, product);

        qnaRepository.save(qna);
    }

    public Page<GetQnaResponseDto> getQnaList(Long productId, Pageable pageable, User user) {
        Product product = getProductEntity(productId);
        Page<Qna> qnaList;
        Page<Qna> qnaListSecret;

        //상품의 유저 정보와 인가된 유저 정보가 같을경우, 판매자 본인
        if (product.getUser().getId().equals(user.getId())) {
            qnaList = qnaRepository.findAllByProduct(product, pageable);
            return qnaList.map(GetQnaResponseDto::of);
            //인가된 유저랑 QnA 비밀글 작성자 본인일 경우
        } else {
            qnaList = qnaRepository.findAllByProductAndSecretQna(product, false, pageable);
            qnaListSecret = qnaRepository.findAllByProductAndUserAndSecretQna(product,
                    user.getId()
                    , true, pageable);

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
        return GetQnaResponseDto.of(getQnaEntity(qnaId));
    }

    @Transactional
    public void updateQna(Long qnaId, QnaRequestDto requestDto, User user) {
        Qna qna = getQnaEntity(qnaId);
        checkQnaUser(user, qna.getUser());
        qna.update(requestDto);
    }

    @Transactional
    public void deleteQna(Long productId, Long qnaId, User user) {
        checkProduct(productId);
        Qna qna = getQnaEntity(qnaId);
        checkQnaUser(user, qna.getUser());
        qnaRepository.delete(qna);
    }

    /**
     * 상품 정보가 있는지 확인
     *
     * @param productId 상품 고유 번호
     */
    public void checkProduct(Long productId) {
        productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("상품 정보가 없습니다.")
        );
    }

    /**
     * 상품 정보가 있는지 확인 후 상품 정보 반환
     *
     * @param productId 상품 고유 번호
     */
    public Product getProductEntity(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("상품 정보가 없습니다.")
        );
    }

    /**
     * Qna 정보를 확인 후 Entity 반환 홥니다
     *
     * @param qnaId Qna 고유 ID
     */
    public Qna getQnaEntity(Long qnaId) {
        return qnaRepository.findById(qnaId).orElseThrow(
                () -> new QnaNotFoundException(ResponseExceptionEnum.QNA_NOT_FOUND)
        );
    }

    /**
     * Qna내의 유저 정보와 인가된 유저 정보를 확인 후 일치 하지 않으면 Exception
     *
     * @param detailUser 인가된 유저 정보
     * @param qnaUser    qnaEntity 유저 정보
     */
    public void checkQnaUser(User detailUser, User qnaUser) {
        if (!detailUser.getId().equals(qnaUser.getId())) {
            throw new QnaCheckUserException(ResponseExceptionEnum.QNA_USER_NOT_VALID);
        }
    }

}
