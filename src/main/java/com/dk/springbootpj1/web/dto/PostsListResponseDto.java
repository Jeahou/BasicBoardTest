package com.dk.springbootpj1.web.dto;

import com.dk.springbootpj1.domain.posts.Posts;
import com.dk.springbootpj1.domain.user.Users;
import lombok.Getter;

@Getter
public class PostsListResponseDto {
    private Long id;
    private String title;
    private Users author;

    public PostsListResponseDto(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
    }
}
