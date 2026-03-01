package com.dk.springbootpj1.service;

import com.dk.springbootpj1.config.auth.dto.SessionUserDto;
import com.dk.springbootpj1.domain.posts.Posts;
import com.dk.springbootpj1.domain.posts.PostsRepository;
import com.dk.springbootpj1.domain.user.Users;
import com.dk.springbootpj1.domain.user.UsersRepository;
import com.dk.springbootpj1.web.dto.PostsResponseDto;
import com.dk.springbootpj1.web.dto.PostsSaveRequestDto;
import com.dk.springbootpj1.web.dto.PostsListResponseDto;
import com.dk.springbootpj1.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto, SessionUserDto sessionUserDto) {
        // 1. 세션 유저의 ID로 DB에서 실제 User 엔티티를 조회합니다.
        Users user = usersRepository.findById(sessionUserDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 조회한 User 엔티티를 담아서 Posts를 저장합니다.
        return postsRepository.save(requestDto.toEntity(user)).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto, SessionUserDto user) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다 id = " + id));
        // 게시글 작성자와 현재 세션 유저의 이름이 다르면 예외 발생
        if (user == null || !posts.getAuthor().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인이 작성한 글만 수정할 수 있습니다.");
        }
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다 id = " + id));
        return new PostsResponseDto(entity);
    }

    @Transactional
    public void delete (Long id, SessionUserDto user) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        if (user == null || !posts.getAuthor().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인이 작성한 글만 삭제할 수 있습니다.");
        }

        postsRepository.delete(posts);
    }

    /*
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }
    */
    //위에껀 리스트를 그냥 쭉 보여주는거 아래는 페이징화 해서 보여주기 jpa page기술이용

    @Transactional(readOnly = true)
    public Page<PostsListResponseDto> findAllDesc(Pageable pageable) {
        return postsRepository.findAll(pageable)
                .map(PostsListResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<PostsListResponseDto> search(String keyword, Pageable pageable) {
        return postsRepository.findByTitleContaining(keyword, pageable)
                .map(PostsListResponseDto::new);
    }

    public List<Posts> findAllPosts() {
        return postsRepository.findAll();
    }
}
