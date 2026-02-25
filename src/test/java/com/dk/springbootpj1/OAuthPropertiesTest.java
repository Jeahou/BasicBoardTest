package com.dk.springbootpj1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// assertThat을 사용하기 위한 static import (가장 중요)
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("oauth")
class OAuthPropertiesTest {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Test
    void 구글_설정_값_불러오기_테스트() {
        // 1. Client ID가 제대로 로드되었는지 확인
        assertThat(clientId).isNotNull();
        assertThat(clientId).contains("apps.googleusercontent.com");
        System.out.println("로그 - 불러온 Client ID: " + clientId);

        // 2. Client Secret이 제대로 로드되었는지 확인 (*로 시작하는지 체크)
        assertThat(clientSecret).isNotNull();
        assertThat(clientSecret).startsWith("*");
        System.out.println("로그 - 불러온 Client Secret: " + clientSecret);
    }
}