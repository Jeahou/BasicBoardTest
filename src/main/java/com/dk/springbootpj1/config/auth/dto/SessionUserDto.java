package com.dk.springbootpj1.config.auth.dto;

import com.dk.springbootpj1.domain.user.Users;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUserDto implements Serializable {

    private Long id;
    private String userName;
    private String nickName;
    private String email;
    private String picture;

    public SessionUserDto(Users user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
