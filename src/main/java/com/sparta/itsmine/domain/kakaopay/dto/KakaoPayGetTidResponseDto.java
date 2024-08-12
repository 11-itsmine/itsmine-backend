package com.sparta.itsmine.domain.kakaopay.dto;

import com.sparta.itsmine.domain.kakaopay.entity.KakaoPayTid;
import lombok.Getter;

@Getter
public class KakaoPayGetTidResponseDto {

    private String tid;

    public KakaoPayGetTidResponseDto(String tid) {
        this.tid=tid;
    }
}
