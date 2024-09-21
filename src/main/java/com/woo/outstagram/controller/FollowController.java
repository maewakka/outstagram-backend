package com.woo.outstagram.controller;

import com.woo.outstagram.dto.follow.UserListResponseDto;
import com.woo.outstagram.util.auth.CurrentUser;
import com.woo.outstagram.entity.user.User;
import com.woo.outstagram.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    @GetMapping("/user-list")
    public UserListResponseDto getUserList(@CurrentUser User user) {
        return followService.getUserList(user);
    }

    @GetMapping("/follow")
    public UserListResponseDto follow(@CurrentUser User user, @RequestParam(value = "followingEmail", required = false) String followingEmail) {
        return followService.saveFollow(user, followingEmail);
    }

    @DeleteMapping("/follow")
    public UserListResponseDto unFollow(@CurrentUser User user, @RequestParam(value = "followingEmail") String followingEmail) {
        return followService.deleteFollow(user, followingEmail);
    }
}
