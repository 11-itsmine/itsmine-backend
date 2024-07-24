package com.sparta.itsmine.domain.productImages.controller;

import com.sparta.itsmine.domain.productImages.Service.ProductImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class ProductImagesController {

    private final ProductImagesService productImagesService;

    // 파일 업로드 엔드포인트
    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("file") List<MultipartFile> files) {
        try {
            List<String> fileUrls = productImagesService.saveFiles(files);
            return ResponseEntity.ok(fileUrls); // S3에 저장된 파일 URL 리스트 반환
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Collections.singletonList("파일 업로드 중 오류가 발생했습니다."));
        }
    }

    // 파일 업로드 엔드포인트
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            String fileUrl = s3Service.saveFile(file);
//            return ResponseEntity.ok(fileUrl); // S3에 저장된 파일 URL 반환
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body("파일 업로드 중 오류가 발생했습니다.");
//        }
//    }

    // 파일 삭제 엔드포인트
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        try {
            productImagesService.deleteFile(fileUrl);
            return ResponseEntity.ok("파일 삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("파일 삭제 중 오류가 발생했습니다.");
        }
    }

    // 파일 다운로드 엔드포인트
//    @GetMapping("/download")
//    public ResponseEntity<?> downloadFile(@RequestParam("filename") String filename) {
//        try {
//            return s3Service.downloadFile(filename); // 파일 다운로드
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("파일 다운로드 중 오류가 발생했습니다.");
//        }
//    }

    // 프로필 업로드 엔드포인트
//    @PostMapping("/upload/profile")
//    public ResponseEntity<String> uploadProfile(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) {
//        try {
//            UserDetailsImpl userDetails = new UserDetailsImpl(userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다.")));
//            String responseMessage = s3Service.uploadProfile(file, userDetails);
//            return ResponseEntity.ok(responseMessage); // 프로필 업로드 완료 메시지 반환
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("프로필 업로드 중 오류가 발생했습니다.");
//        }
//    }
}
