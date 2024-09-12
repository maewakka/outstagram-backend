package com.woo.outstagram.service;

import com.woo.exception.util.BizException;
import com.woo.outstagram.dto.user.LoginRequestDto;
import com.woo.outstagram.dto.user.SignUpRequestDto;
import com.woo.outstagram.entity.user.User;
import com.woo.outstagram.repository.user.UserRepository;
import io.minio.MinioClient;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MinioClient minioClient;

    @Transactional
    public void join(SignUpRequestDto requestDto) {
        if(userRepository.existsByEmail(requestDto.getEmail())) throw new BizException("email_already_exist");

        userRepository.save(requestDto.toEntity(passwordEncoder.encode(requestDto.getPassword())));
    }

    @Transactional(readOnly = true)
    public void login(HttpSession session, LoginRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User savedUser = userRepository.findByEmail(email).orElseThrow(() -> new BizException("login_fail"));
        if(!passwordEncoder.matches(password, savedUser.getPassword())) throw new BizException("login_fail");

        session.setAttribute("userInfo", savedUser);
        session.setMaxInactiveInterval(3600);
    }
}
