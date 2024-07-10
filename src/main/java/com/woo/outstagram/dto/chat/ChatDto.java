package com.woo.outstagram.dto.chat;

import com.woo.outstagram.entity.chat.Chat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatDto {

    private String email;
    private String profileUrl;
    private String nickname;
    private String content;
    private LocalDateTime createdDate;

    @Builder
    public ChatDto(String email, String profileUrl, String nickname, String content, LocalDateTime createdDate) {
        this.email = email;
        this.profileUrl = profileUrl;
        this.nickname = nickname;
        this.content = content;
        this.createdDate = createdDate;
    }

    public static ChatDto toDto(Chat chat, String profileUrl) {
        return ChatDto.builder()
                .email(chat.getUser().getEmail())
                .content(chat.getContent())
                .createdDate(chat.getCreatedDate())
                .nickname(chat.getUser().getNickname())
                .profileUrl(profileUrl)
                .build();
    }
}
