package com.woo.outstagram.controller;

import com.woo.outstagram.dto.post.PostChatRequestDto;
import com.woo.outstagram.dto.post.PostChatResponseDto;
import com.woo.outstagram.dto.post.PostResponseDto;
import com.woo.outstagram.dto.post.UploadPostRequestDto;
import com.woo.outstagram.util.auth.CurrentUser;
import com.woo.outstagram.entity.user.User;
import com.woo.outstagram.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<String> uploadPost(@CurrentUser User user, @Valid @ModelAttribute UploadPostRequestDto requestDto) {
        postService.uploadPost(user, requestDto);

        return ResponseEntity.ok().body("게시물이 성공적으로 업로드 되었습니다.");
    }

    @GetMapping("/post-list")
    public PostResponseDto getPostList(@CurrentUser User user) {
        return postService.getPostList(user);
    }

    @GetMapping("/post-my-list")
    public PostResponseDto getMyPostList(@CurrentUser User user) {
        return postService.getMyPostList(user);
    }

    @DeleteMapping("/post")
    public PostResponseDto deletePost(@CurrentUser User user, @RequestParam(value = "postId") Long postId) {
        return postService.deletePost(user, postId);
    }

    @GetMapping("/like")
    public PostResponseDto setPostLike(@CurrentUser User user, @RequestParam(value = "postId") Long postId) {
        return postService.setPostLike(user, postId);
    }

    @DeleteMapping("/like")
    public PostResponseDto deletePostLike(@CurrentUser User user, @RequestParam(value = "postId") Long postId) {
        return postService.deletePostLike(user, postId);
    }

    @GetMapping("/posts/chat")
    public PostChatResponseDto getPostChatList(@RequestParam(value = "postId") Long postId) {
        return postService.getPostChatList(postId);
    }

    @PostMapping("/posts/chat")
    public PostChatResponseDto setPostChat(@CurrentUser User user, @RequestBody PostChatRequestDto requestDto) {
        return postService.setPostChat(user, requestDto);
    }

    @DeleteMapping("/posts/chat")
    public PostChatResponseDto deletePostChat(@RequestParam(value = "postId") Long postId, @RequestParam(value = "chatId") Long chatId) {
        return postService.deletePostChat(postId, chatId);
    }

    @GetMapping("/posts/search")
    public PostResponseDto getSearchPostList(@CurrentUser User user, @RequestParam(value = "query") String query) {
        return postService.getSearchPostList(user, query);
    }
}
