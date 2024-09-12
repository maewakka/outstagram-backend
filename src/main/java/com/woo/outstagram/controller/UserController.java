package com.woo.outstagram.controller;

import com.woo.outstagram.dto.user.LoginRequestDto;
import com.woo.outstagram.dto.user.LoginResponseDto;
import com.woo.outstagram.dto.user.SignUpRequestDto;
import com.woo.outstagram.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<String> login(HttpSession session, @Valid @RequestBody LoginRequestDto requestDto) {
        userService.login(session, requestDto);

        return ResponseEntity.ok("로그인이 되었습니다.");
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody SignUpRequestDto requestDto) {
        userService.join(requestDto);

        return ResponseEntity.ok().body("회원가입이 정상적으로 진행되었습니다.");
    }
}
