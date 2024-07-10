package com.woo.outstagram.controller;

import com.woo.outstagram.dto.post.PostChatRequestDto;
import com.woo.outstagram.dto.post.UploadPostRequestDto;
import com.woo.outstagram.entity.user.CurrentUser;
import com.woo.outstagram.entity.user.User;
import com.woo.outstagram.service.PostService;
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
    public ResponseEntity uploadPost(@CurrentUser User user, @Valid @ModelAttribute UploadPostRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();

            return ResponseEntity.badRequest().body(errorMsg);
        }

        try {
            postService.uploadPost(user, requestDto);

            return ResponseEntity.ok().body("게시물이 성공적으로 업로드 되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/post-list")
    public ResponseEntity getPostList(@CurrentUser User user) {
        try {
            return ResponseEntity.ok().body(postService.getPostList(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/post-my-list")
    public ResponseEntity getMyPostList(@CurrentUser User user) {
        try {
            return ResponseEntity.ok().body(postService.getMyPostList(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/post")
    public ResponseEntity deletePost(@CurrentUser User user, @RequestParam(value = "postId") Long postId) {
        try {
            return ResponseEntity.ok().body(postService.deletePost(user, postId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/like")
    public ResponseEntity setPostLike(@CurrentUser User user, @RequestParam(value = "postId") Long postId) {
        try {
            return ResponseEntity.ok().body(postService.setPostLike(user, postId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/like")
    public ResponseEntity deletePostLike(@CurrentUser User user, @RequestParam(value = "postId") Long postId) {
        try {
            return ResponseEntity.ok().body(postService.deletePostLike(user, postId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/posts/chat")
    public ResponseEntity getPostChatList(@RequestParam(value = "postId") Long postId) {
        try {
            return ResponseEntity.ok().body(postService.getPostChatList(postId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/posts/chat")
    public ResponseEntity setPostChat(@CurrentUser User user, @RequestBody PostChatRequestDto requestDto) {
        try {
            return ResponseEntity.ok().body(postService.setPostChat(user, requestDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/posts/chat")
    public ResponseEntity deletePostChat(@RequestParam(value = "postId") Long postId, @RequestParam(value = "chatId") Long chatId) {
        try {
            return ResponseEntity.ok().body(postService.deletePostChat(postId, chatId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/posts/search")
    public ResponseEntity getSearchPostList(@CurrentUser User user, @RequestParam(value = "query") String query) {
        try {
            return ResponseEntity.ok().body(postService.getSearchPostList(user, query));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
