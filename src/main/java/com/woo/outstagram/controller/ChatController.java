package com.woo.outstagram.controller;

import com.woo.outstagram.dto.chat.ChatRequestDto;
import com.woo.outstagram.dto.chat.ChatResponseDto;
import com.woo.outstagram.dto.chat.ChatUserListResponseDto;
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
    public ChatUserListResponseDto createChatRoom(@CurrentUser User user, @RequestParam(value = "target") String target) {
        return chatService.createChatRoom(user, target);
    }

    // 채팅 유저 목록
    @GetMapping("/chats/user-list")
    public ChatUserListResponseDto getChatUserList(@CurrentUser User user) {
        return chatService.getChatUserList(user);
    }

    // 채팅방 메세지 송수신
    @MessageMapping("/messages")
    public void chat(ChatRequestDto requestDto) {
        chatService.sendMessage(requestDto);
    }

    // 채팅방 목록
    @GetMapping("/chats/chat-list")
    public ChatResponseDto getChatList(@RequestParam(value = "chatRoomId", required = false) Long chatRoomId) {
        return chatService.getChatList(chatRoomId);
    }

    // 채팅 메세지 내역 저장
    @PostMapping("/chats/chat")
    public ChatResponseDto saveChat(@CurrentUser User user, @RequestBody ChatRequestDto requestDto) {
        return chatService.saveChat(user, requestDto);
    }
}
