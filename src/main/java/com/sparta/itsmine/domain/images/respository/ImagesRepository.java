package com.sparta.itsmine.domain.images.respository;

import com.sparta.itsmine.domain.images.entity.Images;
import com.sparta.itsmine.domain.images.util.ImageType;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagesRepository extends JpaRepository<Images, Long> {
    Images findByUserAndContentType(User user, ImageType imageType);

    Optional<Images> findByIdAndProduct(Long imageId, Product product);
}
