package com.sparta.itsmine.domain.productImages.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImagesService {

    private final AmazonS3 amazonS3;

    @Value("${CLOUD_AWS_S3_BUCKET}")
    private String bucket;

    // 여러 파일 업로드 메소드
    public List<String> saveFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            String fileUrl = saveFile(file);
            fileUrls.add(fileUrl);
        }
        return fileUrls;
    }

    // 파일 저장하고 뽑아오기
    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
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
            throw new IllegalArgumentException("Invalid file URL", e);
        }
    }

    // 파일 다운로드 메소드
    public ResponseEntity<UrlResource> downloadFile(String originalFilename) {
        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, originalFilename));

        String contentDisposition = "attachment; filename=\"" + originalFilename + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

    // 이미지 주소에서 파일 키 추출 메소드
//    private String extractKeyFromImageAddress(String imageAddress) {
//        try {
//            URL url = new URL(imageAddress);
//            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
//            return decodingKey.substring(1);
//        } catch (MalformedURLException | UnsupportedEncodingException e) {
//            throw new IllegalArgumentException("URL 변환 실패", e);
//        }
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

