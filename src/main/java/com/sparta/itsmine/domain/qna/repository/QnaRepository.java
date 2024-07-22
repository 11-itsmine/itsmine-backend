package com.sparta.itsmine.domain.qna.repository;

import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.qna.entity.Qna;
import com.sparta.itsmine.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<Qna, Long> {

    Page<Qna> findAllByProduct(Product product, Pageable pageable);

    Page<Qna> findAllByProductAndUserAndSecretQna(Product product, User user, boolean secretQna,
            Pageable pageable);

    Page<Qna> findAllByProductAndSecretQna(Product product, boolean secretQna,
            Pageable pageable);
}
