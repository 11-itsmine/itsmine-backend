package com.sparta.itsmine.domain.kakaopay.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kakaopay
 */
@Getter
@Setter
public class ReadyResponse {
    private String tid;//결제 고유 번호, 20자
    private Boolean tms_result;
    private String created_at;//결제 준비 요청 시간
    private String next_redirect_pc_url;//요청한 클라이언트가 PC 웹일 경우 카카오톡으로 결제 요청 메시지(TMS)를 보내기 위한 사용자 정보 입력 화면 Redirect URL
    private String next_redirect_mobile_url;//요청한 클라이언트가 모바일 웹일 경우 카카오톡 결제 페이지 Redirect URL
    private String next_redirect_app_url;//요청한 클라이언트(Client)가 모바일 앱일 경우 카카오톡 결제 페이지 Redirect URL
    private String android_app_scheme;//카카오페이 결제 화면으로 이동하는 Android 앱 스킴(Scheme) - 내부 서비스용
    private String ios_app_scheme;//카카오페이 결제 화면으로 이동하는 iOS 앱 스킴 - 내부 서비스용

}
