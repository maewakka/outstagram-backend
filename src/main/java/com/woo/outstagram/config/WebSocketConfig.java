package com.woo.outstagram.config;

import com.woo.outstagram.util.websocket.StompHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 웹 소켓 관련 설정
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Stomp 프로토콜을 사용하여 Websocket 구성
    private final StompHandler stompHandler;
    private final MyStompHandler myStompHandler;

    /**
     * subscribe로 들어오는 URL을 구독 쪽으로 넘겨준다.
     * publish URL로 웹소켓을 생성한다.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/subscribe");
        registry.setApplicationDestinationPrefixes("/publish");
    }

    /**
     * 소켓 연결의 시작 포인트는 socket이다
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    /**
     * JWT 인증 처리를 위한 Handler를 추가해준다.
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registry) {
        registry.interceptors(stompHandler);
    }
}
