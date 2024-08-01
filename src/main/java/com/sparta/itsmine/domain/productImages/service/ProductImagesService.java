package com.sparta.itsmine.domain.productImages.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.productImages.dto.ProductImagesRequestDto;
import com.sparta.itsmine.domain.productImages.entity.ProductImages;
import com.sparta.itsmine.domain.productImages.respository.ProductImagesRepository;
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
public class ProductImagesService {

    private final AmazonS3 amazonS3;
    private final ProductImagesRepository productImagesRepository;

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

    // ProductImages 엔티티 생성 및 저장
    public void createProductImages(ProductImagesRequestDto imagesRequestDto, Product product) {
        List<String> imagesUrl = imagesRequestDto.getImagesUrl();
        for (String imageUrl : imagesUrl) {
            ProductImages productImages = new ProductImages(imageUrl, product);
            product.getProductImagesList().add(productImages);
            productImagesRepository.save(productImages);
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


    // 프로필 업로드 메소드
//    @Transactional
//    public String uploadProfile(MultipartFile file, UserDetailsImpl userDetails) {
//        String originalFilename = file.getOriginalFilename();
//        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;
//
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentType(file.getContentType());
//        metadata.setContentLength(file.getSize());
//
//        String profileURL;
//        try {
//            amazonS3.putObject(bucket, s3FileName, file.getInputStream(), metadata);
//            profileURL = amazonS3.getUrl(bucket, s3FileName).toString();
//        } catch (IOException e) {
//            log.error("업로드 중 오류가 발생했습니다.", e);
//            throw new IllegalArgumentException("업로드 중 오류가 발생했습니다.");
//        }
//
//        User user = userDetails.getUser();
//
//        if (user.getProfileUrl() != null) {
//            deleteFile(user.getProfileUrl());
//        }
//
//        user.updateProfileUrl(profileURL);
//        userRepository.save(user);
//
//        return "프로필 업로드 완료";
//    }
}

