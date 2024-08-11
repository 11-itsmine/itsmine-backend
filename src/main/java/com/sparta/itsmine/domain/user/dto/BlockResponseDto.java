package com.sparta.itsmine.domain.user.dto;

import com.sparta.itsmine.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class BlockResponseDto {

    private Long userId;
    private String username;
    private String name;
    private String nickname;
    private String email;
    private String address;
    private String benReson;
    private LocalDateTime blockedAt;
    private List<String> imageUrls;

    public BlockResponseDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.benReson = user.getBenReason();
        this.blockedAt = user.getBlockedAt();
        this.imageUrls = user.getImageUrls();
    }
}
