package com.dk.springbootpj1.web.dto;

import com.dk.springbootpj1.domain.posts.Posts;
import com.dk.springbootpj1.domain.user.Users;
import lombok.Getter;

@Getter
public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;
    private Users author;

    public PostsResponseDto(Posts entity){
       this.id = entity.getId();
       this.title = entity.getTitle();
       this.content = entity.getContent();
       this.author = entity.getAuthor();
    }
}
