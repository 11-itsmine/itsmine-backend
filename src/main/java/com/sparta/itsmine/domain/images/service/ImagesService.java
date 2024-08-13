package com.sparta.itsmine.domain.images.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.itsmine.domain.images.dto.ProductImagesRequestDto;
import com.sparta.itsmine.domain.images.dto.ProfileImagesResponseDto;
import com.sparta.itsmine.domain.images.entity.Images;
import com.sparta.itsmine.domain.images.respository.ImagesRepository;
import com.sparta.itsmine.domain.images.util.ImageType;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.global.exception.productimages.InvalidURLException;
import com.sparta.itsmine.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.INVALID_URL_EXCEPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImagesService {

    private final AmazonS3 amazonS3;
    private final ImagesRepository imagesRepository;

    @Value("${CLOUD_AWS_S3_DOMAIN}")
    private String CLOUD_FRONT_DOMAIN_NAME;

    @Value("${CLOUD_AWS_S3_BUCKET}")
    private String bucket;

    // 파일 저장하고 뽑아오기
    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return CLOUD_FRONT_DOMAIN_NAME+"/"+originalFilename;
    }

    // ProductImages 엔티티 생성 및 저장
    public void createProductImages(ProductImagesRequestDto imagesRequestDto, Product product) {
        List<String> imagesUrl = imagesRequestDto.getImagesUrl();
        for (String imageUrl : imagesUrl) {
            Images images = new Images(imageUrl, ImageType.PRODUCT ,product);
            product.getImagesList().add(images);
            imagesRepository.save(images);
        }
    }

    // 프로필 업로드 메소드
    @Transactional
    public ProfileImagesResponseDto uploadProfile(MultipartFile file, UserDetailsImpl userDetails) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3.putObject(bucket, s3FileName, file.getInputStream(), metadata);
        String profileURL = CLOUD_FRONT_DOMAIN_NAME + "/" + s3FileName;

        User user = userDetails.getUser();

        // 이전 프로필 이미지 삭제
        Images existingProfileImage = imagesRepository.findByUserAndContentType(user, ImageType.PROFILE);
        if (existingProfileImage != null) {
            deleteFile(existingProfileImage.getImagesUrl());
            imagesRepository.delete(existingProfileImage);
        }

        // 새로운 프로필 이미지 저장
        Images profileImage = new Images(profileURL, ImageType.PROFILE, user);
        imagesRepository.save(profileImage);

        return new ProfileImagesResponseDto(profileURL);
    }

    // 파일 삭제 메소드
    public void deleteFile(String fileUrl) {
        String key = extractKeyFromUrl(fileUrl);
        amazonS3.deleteObject(bucket, key);
    }

    // URL에서 파일 키 추출 메소드
    private String extractKeyFromUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();
            return path.substring(1);
        } catch (MalformedURLException e) {
            log.error("Invalid URL: " + fileUrl, e);
            throw new InvalidURLException(INVALID_URL_EXCEPTION);
        }
    }

    // 선택한 이미지 삭제
    @Transactional
    public void deleteSelectedImages(List<Long> imageIds) {
        List<Images> images = imagesRepository.findAllById(imageIds);
        for (Images image : images) {
            deleteFile(image.getImagesUrl());
        }
        imagesRepository.deleteAll(images);
    }

    // 새로운 이미지 업데이트 메서드 (이미지 추가 및 삭제 처리)
    @Transactional
    public void updateProductImages(Product product, ProductImagesRequestDto imagesRequestDto, List<Long> imagesToDelete) {
        // 1. 삭제할 이미지 처리
        if (imagesToDelete != null && !imagesToDelete.isEmpty()) {
            for (Long imageId : imagesToDelete) {
                Images image = imagesRepository.findByIdAndProduct(imageId, product)
                        .orElseThrow(() -> new IllegalArgumentException("해당 이미지가 존재하지 않습니다."));
                deleteFile(image.getImagesUrl()); // S3에서 파일 삭제
                imagesRepository.delete(image); // DB에서 삭제
            }
        }

        // 2. 새로운 이미지 추가
        if (imagesRequestDto != null && imagesRequestDto.getImagesUrl() != null) {
            for (String imageUrl : imagesRequestDto.getImagesUrl()) {
                Images images = new Images(imageUrl, ImageType.PRODUCT, product);
                product.getImagesList().add(images);
                imagesRepository.save(images);
            }
        }
    }

    //    // 여러 파일 업로드 메소드
//    public List<String> saveFiles(List<MultipartFile> multipartFiles) throws IOException {
//        List<String> fileUrls = new ArrayList<>();
//        for (MultipartFile file : multipartFiles) {
//            String fileUrl = saveFile(file);
//            fileUrls.add(fileUrl);
//        }
//        return fileUrls;
//    }
}


