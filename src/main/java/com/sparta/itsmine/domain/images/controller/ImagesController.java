package com.sparta.itsmine.domain.images.controller;

import com.sparta.itsmine.domain.images.dto.ProductImagesResponseDto;
import com.sparta.itsmine.domain.images.service.ImagesService;
import com.sparta.itsmine.domain.user.repository.UserRepository;
import java.io.IOException;
import java.util.Collections;

import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;
import com.sparta.itsmine.global.exception.DataNotFoundException;
import com.sparta.itsmine.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.sparta.itsmine.global.common.response.ResponseExceptionEnum.USER_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class ImagesController {

    private final ImagesService imagesService;
    private final UserRepository userRepository;

    // 파일 업로드 엔드포인트
    @PostMapping("/upload")
    public ResponseEntity<ProductImagesResponseDto> uploadFile(
            @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = imagesService.saveFile(file);
            return ResponseEntity.ok(
                    new ProductImagesResponseDto(Collections.singletonList(fileUrl)));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ProductImagesResponseDto(
                    Collections.singletonList("파일 업로드 중 오류가 발생했습니다.")));
        }
    }

    //프로필 업로드 엔드포인트
    @PostMapping("/upload/profile")
    public ResponseEntity<String> uploadProfile(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) {
        try {
            UserDetailsImpl userDetails = new UserDetailsImpl(userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(USER_NOT_FOUND)));
            String responseMessage = imagesService.uploadProfile(file, userDetails);
            return ResponseEntity.ok(responseMessage); // 프로필 업로드 완료 메시지 반환
        } catch (Exception e) {
            return ResponseEntity.status(500).body("프로필 업로드 중 오류가 발생했습니다.");
        }
    }

    // 파일 삭제 엔드포인트
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        try {
            imagesService.deleteFile(fileUrl);
            return ResponseEntity.ok("파일 삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("파일 삭제 중 오류가 발생했습니다.");
        }
    }
}
