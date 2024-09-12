package com.woo.outstagram.controller;

import com.woo.outstagram.dto.chat.ChatRequestDto;
import com.woo.outstagram.util.auth.CurrentUser;
import com.woo.outstagram.entity.user.User;
import com.woo.outstagram.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

/**
 * 채팅 기능 관련 컨트롤러
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 채팅방 생성
    @GetMapping("/chats/create")
    public ResponseEntity<? extends Object> createChatRoom(@CurrentUser User user, @RequestParam(value = "target") String target) throws Exception {
        try {
            return ResponseEntity.ok().body(chatService.createChatRoom(user, target));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("요청에 오류가 발생하였습니다.");
        }
    }

    // 채팅 유저 목록
    @GetMapping("/chats/user-list")
    public ResponseEntity getChatUserList(@CurrentUser User user) {
        try {
            return ResponseEntity.ok().body(chatService.getChatUserList(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("요청에 오류가 발생하였습니다.");
        }
    }

    // 채팅방 메세지 송수신
    @MessageMapping("/messages")
    public void chat(ChatRequestDto requestDto) {
        chatService.sendMessage(requestDto);
    }

    // 채팅방 목록
    @GetMapping("/chats/chat-list")
    public ResponseEntity getChatList(@RequestParam(value = "chatRoomId", required = false) Long chatRoomId) {
        try {
            return ResponseEntity.ok().body(chatService.getChatList(chatRoomId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("요청에 오류가 발생하였습니다.");
        }
    }

    // 채팅 메세지 내역 저장
    @PostMapping("/chats/chat")
    public ResponseEntity saveChat(@CurrentUser User user, @RequestBody ChatRequestDto requestDto) {
        try {
            return ResponseEntity.ok().body(chatService.saveChat(user, requestDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("요청에 오류가 발생하였습니다.");
        }
    }
}
