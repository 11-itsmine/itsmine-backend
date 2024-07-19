package com.sparta.itsmine.domain.qna.repository;

import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.qna.entity.Qna;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<Qna, Long> {

    List<Qna> findAllByProduct(Product product);
}
