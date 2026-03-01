package com.dk.springbootpj1.web;


import com.dk.springbootpj1.config.auth.dto.SessionUserDto;
import com.dk.springbootpj1.service.PostsService;
import com.dk.springbootpj1.web.dto.PostsResponseDto;
import com.dk.springbootpj1.web.dto.PostsSaveRequestDto;
import com.dk.springbootpj1.web.dto.PostsUpdateRequestDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @PostMapping("/api/v1/posts")
    public Long save(@Valid @RequestBody PostsSaveRequestDto requestDto){
        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");
        return postsService.save(requestDto, user);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id,@Valid @RequestBody PostsUpdateRequestDto requestDto){
        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");
        return postsService.update(id, requestDto, user);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id){
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");

        postsService.delete(id, user);
        return id;
    }
}
