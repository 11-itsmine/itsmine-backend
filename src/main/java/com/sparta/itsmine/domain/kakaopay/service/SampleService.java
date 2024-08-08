package com.sparta.itsmine.domain.kakaopay.service;


import com.sparta.itsmine.domain.kakaopay.dto.ApproveRequest;
import com.sparta.itsmine.domain.kakaopay.dto.ReadyRequest;
import com.sparta.itsmine.domain.kakaopay.dto.ReadyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kakaopay
 */
@Service
public class SampleService {
    @Value("${kakaopay.api.secret.key}")
    private String kakaopaySecretKey;

    @Value("${cid}")
    private String cid;

    @Value("${sample.host}")
    private String sampleHost;

    private String tid;

    public ReadyResponse ready(String agent, String openType) {
        // Request header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request param
        ReadyRequest readyRequest = ReadyRequest.builder()
                .cid(cid)//가맹점 코드, 10자
                .partnerOrderId("1")//가맹점 주문번호, 최대 100자
                .partnerUserId("1")//가맹점 회원 id, 최대 100자
                .itemName("좆소고양이")//상품명, 최대 100자
                .quantity(1)//상품 수량
                .totalAmount(1100)//상품 총액
                .taxFreeAmount(0)//상품 비과세 금액
                .vatAmount(100)//상품 부가세 금액
                .approvalUrl(sampleHost + "/approve/" + agent + "/" + openType)//결제 성공 시 redirect url, 최대 255자
                .cancelUrl(sampleHost + "/cancel/" + agent + "/" + openType)//결제 취소 시 redirect url, 최대 255자
                .failUrl(sampleHost + "/fail/" + agent + "/" + openType)//결제 실패 시 redirect url, 최대 255자
                .build();

        // Send reqeust
        HttpEntity<ReadyRequest> entityMap = new HttpEntity<>(readyRequest, headers);
        ResponseEntity<ReadyResponse> response = new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                entityMap,
                ReadyResponse.class
        );
        ReadyResponse readyResponse = response.getBody();

        // 주문번호와 TID를 매핑해서 저장해놓는다.
        // Mapping TID with partner_order_id then save it to use for approval request.
        this.tid = readyResponse.getTid();
        return readyResponse;
    }

    public String approve(String pgToken) {
        // ready할 때 저장해놓은 TID로 승인 요청
        // Call “Execute approved payment” API by pg_token, TID mapping to the current payment transaction and other parameters.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request param
        ApproveRequest approveRequest = ApproveRequest.builder()
                .cid(cid)
                .tid(tid)
                .partnerOrderId("1")
                .partnerUserId("1")
                .pgToken(pgToken)
                .build();

        // Send Request
        HttpEntity<ApproveRequest> entityMap = new HttpEntity<>(approveRequest, headers);
        try {
            ResponseEntity<String> response = new RestTemplate().postForEntity(
                    "https://open-api.kakaopay.com/online/v1/payment/approve",
                    entityMap,
                    String.class
            );

            // 승인 결과를 저장한다.
            // save the result of approval
            String approveResponse = response.getBody();
            return approveResponse;
        } catch (HttpStatusCodeException ex) {
            return ex.getResponseBodyAsString();
        }
    }
}
