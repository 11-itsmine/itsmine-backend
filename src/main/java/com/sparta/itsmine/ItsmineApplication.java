package com.sparta.itsmine;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ItsmineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsmineApplication.class, args);
    }

    @Bean//빈 등록
    public ServletWebServerFactory servletContainer() {//ServletWebServerFactory를 구현한 TomcatServletWebServerFactory를 생성하여 반환
        // Enable SSL Trafic
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override//postProcessContext 메소드를 오버라이드하여 Tomcat 컨텍스트에 보안 제약을 추가(모든 요청(/*)에 대해 HTTPS를 요구)
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };

        // Add HTTP to HTTPS redirect
        //httpToHttpsRedirectConnector 메소드를 호출하여 HTTP 포트에서 HTTPS 포트로의 리디렉션 추가
        tomcat.addAdditionalTomcatConnectors(httpToHttpsRedirectConnector());

        return tomcat;
    }

    /*
    We need to redirect from HTTP to HTTPS. Without SSL, this application used
    port 8082. With SSL it will use port 8443. So, any request for 8082 needs to be
    redirected to HTTPS on 8443.
     */
    private Connector httpToHttpsRedirectConnector() {
        //Connector 객체를 생성하여 HTTP 포트(8080)에서 들어오는 요청을 HTTPS 포트(443)로 리디렉션
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        //setScheme("http"): 이 커넥터는 HTTP 스키마를 사용
        connector.setScheme("http");
        //setPort(8080): HTTP 요청을 수신하는 포트
        connector.setPort(8080);
        //setSecure(false): 이 커넥터는 시큐리티 대상에서 제외
        connector.setSecure(false);
        //setRedirectPort(443): 8080 포트로 들어오는 모든 요청을 443 포트로 리디렉션
        connector.setRedirectPort(443);
        return connector;
    }

//    참고자료:
//    https://minholee93.tistory.com/entry/Spring-Security-Enable-SSLHTTPS-Spring-Boot-5?category=924032
//    https://velog.io/@code12/Spring-Security-SSLHTTPS-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0

}
