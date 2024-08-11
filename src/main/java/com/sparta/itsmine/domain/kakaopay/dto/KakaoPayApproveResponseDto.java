package com.sparta.itsmine.domain.kakaopay.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

@Getter
public class KakaoPayApproveResponseDto {
    private String aid;//요청 고유 번호 - 승인/취소가 구분된 결제번호
    private String tid;//결제 고유 번호 - 승인/취소가 동일한 결제번호
    private String cid;//가맹점 코드
    private String sid;//정기 결제용 ID, 정기 결제 CID로 단건 결제 요청 시 발급
    private String partner_order_id;//가맹점 주문번호, 최대 100자
    private String partner_used_id;//가맹점 회원 id, 최대 100자
    private String payment_method_type;//결제 수단, CARD 또는 MONEY 중 하나
    private Amount amount;
    @Getter
    @Setter
    public static class Amount{
        private Integer total;//전체 결제 금액
        private Integer tax_free;//비과세 금액
        private Integer vat;//부가세 금액
        private Integer point;//사용한 포인트 금액
        private Integer discount;//할인 금액
        private Integer green_deposit;//컵 보증금?
    }
    private CardInfo card_info;//결제 상세 정보, 결제 수단이 카드일 경우만 포함
    @Getter
    @Setter
    public static class CardInfo{
        private String kakaopay_purchase_corp;//카카오페이 매입사명
        private String kakaopay_purchase_corp_code;//카카오페이 매입사 코드
        private String kakaopay_issuer_corp;//카카오페이 발급사명
        private String kakaopay_issuer_corp_code;//카카오페이 발급사 코드
        private String bin;//카드 BIN
        private String card_type;//카드 타입
        private String install_month;//할부 개월 수
        private String approved_id;//카드사 승인번호
        private String card_mid;//카드사 가맹점 번호
        private String interest_free_install;//무이자할부 여부(Y/N)
        private String installment_type;//할부 유형(24.02.01일부터 제공): CARD_INSTALLMENT: 업종 무이자, SHARE_INSTALLMENT: 분담 무이자
        private String card_item_code;//카드 상품 코드
    }
    private String item_name;//상품 이름, 최대 100자
    private String item_code;//상품 코드, 최대 100자
    private Integer quantity;//상품 수량
    private LocalDateTime created_at;//결제 준비 요청 시각
    private LocalDateTime approved_at;//결제 승인 시각
    private String payload;//결제 승인 요청에 대해 저장한 값, 요청 시 전달된 내용
}