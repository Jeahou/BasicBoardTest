package com.dk.springbootpj1.config.auth.dto;

import com.dk.springbootpj1.domain.user.Role;
import com.dk.springbootpj1.domain.user.Users;
import com.dk.springbootpj1.util.NicknameGenerator;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthDto {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String userName;
    private String nickName;
    private String email;
    private String picture;

    @Builder
    public OAuthDto(Map<String, Object> attributes, String nameAttributeKey, String userName, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.userName = userName;
        this.email = email;
        this.picture = picture;
    }

    // 구글에서 오는 정보는 Map 형태이므로 하나하나 변환해야 합니다.
    public static OAuthDto of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthDto.builder()
                .userName((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Users toEntity() {
        return Users.builder()
                .userName(userName)
                .nickName(NicknameGenerator.generate())
                .email(email)
                .picture(picture)
                .role(Role.USER) // 기본 권한은 GUEST로 설정
                .build();
    }

}
