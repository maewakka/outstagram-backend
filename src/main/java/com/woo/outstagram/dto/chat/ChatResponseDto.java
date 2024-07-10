package com.woo.outstagram.dto.chat;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ChatResponseDto {

    private List<ChatDto> chatList;

    @Builder
    public ChatResponseDto(List<ChatDto> chatList) {
        this.chatList = chatList;
    }
}
