package com.dk.springbootpj1.config;

import com.dk.springbootpj1.config.auth.CustomOAuth2UserService;
import com.dk.springbootpj1.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 스프링 시큐리티 설정 활성화
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 보안 설정 (H2 콘솔 사용 및 테스트를 위해 일단 비활성화)
                .csrf(csrf -> csrf.disable())

                // 2. H2 콘솔 등을 사용할 때 프레임 구조를 허용하기 위한 설정
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                )

                // 3. URL별 권한 관리 설정
                .authorizeHttpRequests(auth -> auth
                        // 아래 경로는 로그인 없이 누구나 접근 가능
                        .requestMatchers("/", "/posts/**", "/css/**","/login/**", "/js/**", "/oauth2/**").permitAll() // 페이지 허용

                        //추가: 상세 조회 API는 GET 방식일 때만 비로그인 허용
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/*").permitAll()

                        //나머지 등록, 수정, 삭제 API는 여전히 USER 권한 필요
                        .requestMatchers(new AntPathRequestMatcher("/api/v1/**")).hasRole(Role.USER.name())

                        .anyRequest().authenticated()
                )

                // 4. 로그아웃 설정
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 메인 페이지로 이동
                        .invalidateHttpSession(true) // 세션 무효화 (필수!)
                        .clearAuthentication(true)   // 인증 정보 삭제 (필수!)
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                )

                // 5. OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 핵심!
                        )
                        .defaultSuccessUrl("/", true)
                );

        return http.build();
    }
}