package com.woo.outstagram.dto.chat;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {

    private String email;
    private String nickname;
    private String profileUrl;
    private String content;
    private LocalDateTime sendDate;

    @Builder
    public ChatMessage(String email, String nickname, String profileUrl, String content, LocalDateTime sendDate) {
        this.email = email;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.content = content;
        this.sendDate = sendDate;
    }
}
