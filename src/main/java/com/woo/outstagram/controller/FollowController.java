package com.woo.outstagram.controller;

import com.woo.outstagram.entity.user.CurrentUser;
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
    public ResponseEntity getUserList(@CurrentUser User user) {
        try {
            return ResponseEntity.ok().body(followService.getUserList(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/follow")
    public ResponseEntity follow(@CurrentUser User user, @RequestParam(value = "followingEmail", required = false) String followingEmail) {
        log.info(followingEmail);
        try {
            return ResponseEntity.ok().body(followService.saveFollow(user, followingEmail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/follow")
    public ResponseEntity unFollow(@CurrentUser User user, @RequestParam(value = "followingEmail") String followingEmail) {
        try {
            return ResponseEntity.ok().body(followService.deleteFollow(user, followingEmail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
