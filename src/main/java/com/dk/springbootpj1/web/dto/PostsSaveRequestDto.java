package com.dk.springbootpj1.web.dto;

import com.dk.springbootpj1.domain.posts.Posts;
import com.dk.springbootpj1.domain.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;

    @Builder
    public PostsSaveRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Posts toEntity(Users author){
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }


}
