package com.woo.outstagram.controller;

import com.woo.outstagram.dto.profile.ProfileUpdateRequestDto;
import com.woo.outstagram.dto.profile.UpdatePasswordRequestDto;
import com.woo.outstagram.util.auth.CurrentUser;
import com.woo.outstagram.entity.user.User;
import com.woo.outstagram.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity getProfile(@CurrentUser User user) {
        try {
            return ResponseEntity.ok().body(profileService.getProfile(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("요청에 오류가 발생하였습니다.");
        }
    }

    @GetMapping("/profiles/count")
    public ResponseEntity getProfileCount(@CurrentUser User user) {
        try {
            return ResponseEntity.ok().body(profileService.getProfileCount(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/profiles/thumbnail")
    public ResponseEntity updateProfileThumbnail(@CurrentUser User user, @RequestPart MultipartFile file) {
        try {
            return ResponseEntity.ok().body(profileService.updateProfileThumbnail(user, file));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/profiles/profile")
    public ResponseEntity updateProfiles(@CurrentUser User user, @Valid @RequestBody ProfileUpdateRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();

            return ResponseEntity.badRequest().body(errorMsg);
        }

        try {
            return ResponseEntity.ok().body(profileService.updateProfiles(user, requestDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/password")
    public ResponseEntity updatePassword(@CurrentUser User user, @RequestBody UpdatePasswordRequestDto requestDto) {
        try {
            profileService.updateUserPassword(user, requestDto);
            return ResponseEntity.ok().body("비밀번호가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
