package com.woo.outstagram.dto.chat;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatUserDto {

    private String email;
    private String nickname;
    private String profileUrl;
    private Long chatRoomId;
    private String lastMessage;
    private LocalDateTime modifiedDate;

    @Builder
    public ChatUserDto(String email, String nickname, String profileUrl, Long chatRoomId, String lastMessage, LocalDateTime modifiedDate) {
        this.email = email;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.chatRoomId = chatRoomId;
        this.lastMessage = lastMessage;
        this.modifiedDate = modifiedDate;
    }
}
