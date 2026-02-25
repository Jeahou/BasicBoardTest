package com.dk.springbootpj1.config.auth;


import com.dk.springbootpj1.config.auth.dto.OAuthDto;
import com.dk.springbootpj1.config.auth.dto.SessionUserDto;
import com.dk.springbootpj1.domain.user.Users;
import com.dk.springbootpj1.domain.user.UsersRepository;
import com.dk.springbootpj1.util.NicknameGenerator;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
    private final UsersRepository usersRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        System.out.println("구글 유저 정보: " + oAuth2User.getAttributes());

        // 현재 로그인 진행 중인 서비스를 구분 (구글, 네이버 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2 로그인 진행 시 키가 되는 필드값 (Primary Key와 같은 의미)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // 구글 로그인으로 가져온 사용자의 속성들을 담을 클래스
        OAuthDto attributes = OAuthDto.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Users user = saveOrUpdate(attributes);

        httpSession.setAttribute("user", new SessionUserDto(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 유저 정보가 업데이트 되었을 때를 대비해 update 로직 추가
    private Users saveOrUpdate(OAuthDto attributes) {
        Users user = usersRepository.findByEmail(attributes.getEmail())
                .map(entity -> {
                    // 이미 가입된 유저인데 닉네임이 없다면 이때 부여!
                    if (entity.getNickName() == null) {
                        entity.updateNickname(NicknameGenerator.generate());
                    }
                    return entity.update(attributes.getUserName(), attributes.getPicture());
                })
                .orElse(attributes.toEntity());

        return usersRepository.save(user);
    }
}
