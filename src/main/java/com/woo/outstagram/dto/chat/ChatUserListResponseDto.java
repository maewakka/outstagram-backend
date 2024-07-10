package com.woo.outstagram.dto.chat;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ChatUserListResponseDto {

    List<ChatUserDto> chatRoomList;

    @Builder
    public ChatUserListResponseDto(List<ChatUserDto> chatRoomList) {
        this.chatRoomList = chatRoomList;
    }
}
