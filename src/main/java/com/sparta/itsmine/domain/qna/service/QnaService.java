package com.sparta.itsmine.domain.qna.service;

import static com.sparta.itsmine.domain.user.utils.UserRole.MANAGER;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.INVALID_PASSWORD;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.PRODUCT_NOT_FOUND;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.QNA_NOT_FOUND;
import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.UNAUTHORIZED_ACCESS;

import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.qna.dto.GetQnaResponseDto;
import com.sparta.itsmine.domain.qna.dto.QnaRequestDto;
import com.sparta.itsmine.domain.qna.entity.Qna;
import com.sparta.itsmine.domain.qna.repository.QnaRepository;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.exception.DataNotFoundException;

import java.util.List;
import java.util.Objects;
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
    public GetQnaResponseDto createQna(Long productId, QnaRequestDto requestDTO, User user) {
        Product product = getProduct(productId);
        Qna qna = Qna.of(requestDTO, user, product);
        qnaRepository.save(qna);

        return GetQnaResponseDto.of(qna);
    }

    public Page<GetQnaResponseDto> getQnaList(Long productId, Pageable pageable, User user) {
        Product product = getProduct(productId);

        if (isProductOwner(product, user)) {
            return qnaRepository.findAllByProduct(product, pageable).map(GetQnaResponseDto::of);
        } else {
            Page<Qna> qnaList = qnaRepository.findAllByProductIdAndSecretQna(product.getId(), false, pageable);
            Page<Qna> qnaListSecret = qnaRepository.findAllByProductIdAndUserAndSecretQna(
                    product.getId(), user.getId(), true, pageable);

            List<GetQnaResponseDto> getQnaResponseDtoList = Stream.concat(
                            qnaList.stream().map(GetQnaResponseDto::of),
                            qnaListSecret.stream().map(GetQnaResponseDto::of))
                    .sorted((list, listSecret) -> listSecret.getUpdatedAt().compareTo(list.getUpdatedAt()))
                    .collect(Collectors.toList());

            return new PageImpl<>(getQnaResponseDtoList, pageable, getQnaResponseDtoList.size());
        }
    }

    public GetQnaResponseDto getQna(Long productId, Long qnaId) {
        checkProduct(productId);
        return GetQnaResponseDto.of(getQna(qnaId));
    }

    @Transactional
    public GetQnaResponseDto updateQna(Long qnaId, Long productId, QnaRequestDto requestDto, User user) {
        Qna qna = getQna(qnaId);
        checkUserAuthorization(qna, user);

        validatePassword(requestDto.getPassword(), qna.getPassowrd());

        qna.update(requestDto);

        return GetQnaResponseDto.of(qna);
    }

    @Transactional
    public void deleteQna(Long productId, Long qnaId, String password, User user) {  // 비밀번호를 매개변수로 추가
        checkProduct(productId);
        Qna qna = getQna(qnaId);
        checkUserAuthorization(qna, user);
        validatePassword(password, qna.getPassowrd());  // 비밀번호 검증
        qnaRepository.delete(qna);
    }

    private void validatePassword(String inputPassword, String storedPassword) {
        if (!Objects.equals(inputPassword, storedPassword)) {
            throw new DataNotFoundException(INVALID_PASSWORD);
        }
    }

    private void checkUserAuthorization(Qna qna, User user) {
        if (!qna.getUser().getId().equals(user.getId()) && !user.getUserRole().equals(MANAGER)) {
            throw new DataNotFoundException(UNAUTHORIZED_ACCESS);
        }
    }

    private boolean isProductOwner(Product product, User user) {
        return product.getUser().getId().equals(user.getId());
    }

    public void checkProduct(Long productId) {
        productRepository.findById(productId).orElseThrow(
                () -> new DataNotFoundException(PRODUCT_NOT_FOUND)
        );
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new DataNotFoundException(PRODUCT_NOT_FOUND)
        );
    }

    public Qna getQna(Long qnaId) {
        return qnaRepository.findById(qnaId).orElseThrow(
                () -> new DataNotFoundException(QNA_NOT_FOUND)
        );
    }
}
