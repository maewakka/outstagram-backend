package com.woo.outstagram.service;

import com.woo.outstagram.dto.user.LoginRequestDto;
import com.woo.outstagram.dto.user.SignUpRequestDto;
import com.woo.outstagram.entity.user.User;
import com.woo.outstagram.repository.user.UserRepository;
import com.woo.outstagram.util.jwt.JwtTokenProvider;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MinioClient minioClient;

    /**
     * 회원가입 로직
     * @param requestDto
     */
    @Transactional
    public void join(SignUpRequestDto requestDto) throws Exception {
        if(!userRepository.existsByEmail(requestDto.getEmail())) {
            userRepository.save(requestDto.toEntity(passwordEncoder.encode(requestDto.getPassword())));
        } else {
            throw new Exception("이미 가입된 이메일 입니다.");
        }
    }

    /**
     * 로그인 로직
     * @param LoginRequestDto
     */
    @Transactional
    public String login(LoginRequestDto requestDto) throws Exception {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User savedUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("로그인 정보가 잘못되었습니다."));

        if(passwordEncoder.matches(password, savedUser.getPassword())) {
            String token = jwtTokenProvider.createJwt(email);
            log.info("User [{}] token published -> {}", email, token);
            return token;
        }
        else {
            throw new Exception("로그인 정보가 잘못되었습니다.");
        }
    }
}
