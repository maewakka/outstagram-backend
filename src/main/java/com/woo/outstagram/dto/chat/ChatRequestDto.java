package com.woo.outstagram.dto.chat;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ChatRequestDto {

    private String senderEmail;
    private String receiverEmail;
    private Long chatRoomId;
    private String message;

    @Builder
    public ChatRequestDto(String senderEmail, String receiverEmail, Long chatRoomId, String message) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.chatRoomId = chatRoomId;
        this.message = message;
    }
}
