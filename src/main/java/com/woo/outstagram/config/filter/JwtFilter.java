package com.woo.outstagram.config.filter;

import com.woo.outstagram.util.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private static final String[] PERMIT_URL_ARRAY = {
            "v2",
            "swagger-resources",
            "configuration",
            "swagger-ui",
            "webjars",
            "v3",
            "users",
            "static",
            "socket"
    };
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청 Url의 시작이 PERMIT_URL_ARRAY에 포함되는지 검증하여, 해당되면 다음 필터로 요청을 전달한다.
//        log.info("PATH : {}", request.getServletPath());

        String prePath = (!request.getServletPath().equals("/") ? request.getServletPath().split("/")[1] : "/");

        if(Arrays.asList(PERMIT_URL_ARRAY).contains(prePath)) {
            filterChain.doFilter(request, response);
        } else {
            String token = getToken(request);
//            log.info("Token : {}", token);

            if(StringUtils.hasText(token)) {
                int flag = jwtTokenProvider.validateToken(token);

                // 토큰이 유효할 때, 인증 객체를 생성하여 SecurityContextHolder에 삽입 후, 다음 필터로 넘어간다.
                if(flag == 1) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    doFilter(request, response, filterChain);
                }
                else if (flag == -1) {
                    log.warn("토큰 값이 잘못되었습니다.");

                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setCharacterEncoding("UTF-8");
                }
            } else {
                log.warn("토큰 값이 비었습니다.");

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setCharacterEncoding("UTF-8");
            }
        }

    }
    public String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
//        log.info("bearerToken : {}", bearerToken);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
