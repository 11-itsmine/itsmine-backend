package com.sparta.itsmine.domain.images.respository;

import com.sparta.itsmine.domain.images.entity.Images;
import com.sparta.itsmine.domain.images.util.ImageType;
import com.sparta.itsmine.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagesRepository extends JpaRepository<Images, Long> {
    Images findByUserAndContentType(User user, ImageType imageType);
}
