package com.sparta.itsmine.domain.kakaopay.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoPayCancelRequestDto {

    private String cid;//가맹점 코드, 10자
    private String tid;//결제 고유번호, 20자
    private Integer cancel_amount;//취소 금액
    private Integer cancel_tax_free_amount;//취소 비과세 금액
    private Integer cancel_vat_amount;//취소 부가세 금액
//    승인시 vat_amount를 보냈다면 취소시에도 동일하게 요청
//    승인과 동일하게 요청 시 값을 전달하지 않을 경우 자동계산 (취소 금액 - 취소 비과세 금액)/11, 소숫점이하 반올림
}
