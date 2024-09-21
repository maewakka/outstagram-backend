package com.woo.outstagram.controller;

import com.woo.outstagram.dto.profile.ProfileCountResponseDto;
import com.woo.outstagram.dto.profile.ProfileUpdateRequestDto;
import com.woo.outstagram.dto.profile.UpdatePasswordRequestDto;
import com.woo.outstagram.dto.user.UserDetailResponseDto;
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
    public UserDetailResponseDto getProfile(@CurrentUser User user) {
        return profileService.getProfile(user);
    }

    @GetMapping("/profiles/count")
    public ProfileCountResponseDto getProfileCount(@CurrentUser User user) {
        return profileService.getProfileCount(user);
    }

    @PostMapping("/profiles/thumbnail")
    public UserDetailResponseDto updateProfileThumbnail(@CurrentUser User user, @RequestPart MultipartFile file) {
        return profileService.updateProfileThumbnail(user, file);
    }

    @PostMapping("/profiles/profile")
    public UserDetailResponseDto updateProfiles(@CurrentUser User user, @Valid @RequestBody ProfileUpdateRequestDto requestDto) {
        return profileService.updateProfiles(user, requestDto);
    }

    @PostMapping("/password")
    public ResponseEntity<String> updatePassword(@CurrentUser User user, @RequestBody UpdatePasswordRequestDto requestDto) {
        profileService.updateUserPassword(user, requestDto);
        return ResponseEntity.ok().body("비밀번호가 성공적으로 변경되었습니다.");
    }
}
