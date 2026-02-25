package com.dk.springbootpj1.domain.user;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column
    private String nickName;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING) // JPA로 DB 저장 시 숫자(0,1)가 아닌 문자열로 저장
    @Column(nullable = false)
    private Role role;

    @Builder
    public Users(String userName, String nickName, String email, String picture, Role role) {
        this.userName = userName;
        this.nickName = nickName;
        this.email = email;
        this.picture = picture;
        this.role = role;           
    }

    // 사용자 정보 수정 시 사용할 메서드
    //강제로 role해놨음 나중에 수정
    public Users update(String userName, String picture) {
        this.userName = userName;
        this.picture = picture;
        this.role = Role.USER;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void updateNickname(String nickname) {
        this.nickName = nickname;
    }

}
