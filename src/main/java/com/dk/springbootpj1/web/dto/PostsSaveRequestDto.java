package com.dk.springbootpj1.web.dto;

import com.dk.springbootpj1.domain.posts.Posts;
import com.dk.springbootpj1.domain.user.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

    @NotBlank(message = "제목은 공백일 수 없습니다!.")
    @Size(max = 500, message = "제목은 500자 이내여야합니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입니다.")
    @Size(max = 2000, message = "내용은 2000자 이내여야합니다.")
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
