package com.sparta.itsmine.domain.qna.repository;

import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.qna.entity.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QnaRepository extends JpaRepository<Qna, Long> {



    Page<Qna> findAllByProduct(Product product, Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE q.product.id = :productId AND q.user.id = :userId AND q.secretQna = :secretQna")
    Page<Qna> findAllByProductIdAndUserAndSecretQna(@Param("productId") Long productId,
            @Param("userId") Long userId,
            @Param("secretQna") boolean secretQna,
            Pageable pageable);

    @Query("SELECT q FROM Qna q WHERE q.product.id = :productId AND q.secretQna = :secretQna")
    Page<Qna> findAllByProductIdAndSecretQna(Long productId, boolean secretQna,
            Pageable pageable);
}
