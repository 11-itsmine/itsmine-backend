package com.sparta.itsmine.global.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
public class WebConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Authorization"); // CORS로 인해 프론트단에서 인식하지 못하는 Authrization 헤더를 노출

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    // cors 는 6개만 가져올 수 있다.
    // 노출헤더라는 것을 넣어서 가져가야 하는 헤더를 열어줘야 한다.
    // 보안상 헤더 키를 등록하는 느낌

    // FE 에서는 헤더와 쿠키를 관리 ㄴㄴ

}
