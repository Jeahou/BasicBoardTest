package com.dk.springbootpj1.web;

import com.dk.springbootpj1.config.auth.dto.SessionUserDto;
import com.dk.springbootpj1.service.PostsService;
import com.dk.springbootpj1.web.dto.PostsListResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostsListResponseDto> posts = postsService.findAllDesc(pageable);

        model.addAttribute("posts", posts);

        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");
        // 만약 유저 정보가 있다면 화면에 넘겨줍니다.
        if (user != null) {
            model.addAttribute("userNickName", user.getNickName());
        }

        return "startListPage";
    }

    @GetMapping("/new")
    public String indexNew(Model model) { // 파라미터에서 @LoginUser 삭제 나중에 아래부분 대신 @LoginUser 따로 만들어서 주입으로 통일
        // 기존에 하시던 방식 그대로!
        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("userNickName", user.getNickName());
        }

        return "new";
    }

    @GetMapping("/posts/search")
    public String search(String keyword, Model model,
                         @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");
        // 만약 유저 정보가 있다면 화면에 넘겨줍니다.
        if (user != null) {
            model.addAttribute("userNickName", user.getNickName());
        }

        if (keyword == null || keyword.trim().isEmpty()) {
            return "redirect:/";
        }

        Page<PostsListResponseDto> searchList = postsService.search(keyword, pageable);

        model.addAttribute("posts", searchList);
        model.addAttribute("keyword", keyword); // 화면에서 검색어를 계속 보여주기 위해 추가

        return "startListPage";
    }

    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable Long id, Model model){
        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");

        if (user != null) {
            // 로그인한 유저의 이름을 userName이라는 이름으로 넘김
            model.addAttribute("userNickName", user.getNickName());
        }
        model.addAttribute("post", postsService.findById(id));
        return "view";
    }

    @GetMapping("/posts/edit/{id}")
    public String editPost(@PathVariable Long id, Model model){
        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");

        if (user != null) {
            // 로그인한 유저의 이름을 userName이라는 이름으로 넘김
            model.addAttribute("userNickName", user.getNickName());
        }
        model.addAttribute("post", postsService.findById(id));
        return "edit";
    }

}
