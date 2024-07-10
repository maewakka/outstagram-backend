package com.woo.outstagram.controller;

import com.woo.outstagram.dto.user.LoginRequestDto;
import com.woo.outstagram.dto.user.LoginResponseDto;
import com.woo.outstagram.dto.user.SignUpRequestDto;
import com.woo.outstagram.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();

            return ResponseEntity.badRequest().body(errorMsg);
        }
        try {
            String accessToken = userService.login(requestDto);
            return ResponseEntity.ok().body(LoginResponseDto.builder().accessToken(accessToken).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/join")
    public ResponseEntity join(@Valid @RequestBody SignUpRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();

            return ResponseEntity.badRequest().body(errorMsg);
        }

        try {
            userService.join(requestDto);

            return ResponseEntity.ok().body("회원가입이 정상적으로 진행되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
