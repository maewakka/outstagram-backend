package com.woo.outstagram.service;

import com.woo.outstagram.entity.user.CustomUserDetails;
import com.woo.outstagram.entity.user.User;
import com.woo.outstagram.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 인증 후 Custom한 Authentication 객체인 CustomUserDetails를 반환한다.
     * @param email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        return CustomUserDetails.builder().user(user).build();
    }
}
