package com.woo.outstagram.config;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

@Component
public class MyStompHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("STOMP 연결 성공");
        System.out.println("Session ID: " + session.getSessionId());
        System.out.println("Connected headers: " + connectedHeaders);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("메시지 수신");
        System.out.println("Headers: " + headers);
        System.out.println("Payload: " + payload);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.err.println("STOMP 예외 발생");
        exception.printStackTrace();
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        System.err.println("STOMP 전송 오류");
        exception.printStackTrace();
    }

//    @Override
//    public void handleDisconnection(StompSession session, StompHeaders headers, StompCommand command) {
//        System.out.println("STOMP 연결 해제");
//    }
}