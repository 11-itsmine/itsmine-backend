package com.sparta.itsmine.domain.productImages.respository;

import com.sparta.itsmine.domain.productImages.entity.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImagesRepository extends JpaRepository<ProductImages, Long> {
}
