package com.woo.outstagram.dto.post;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PostChatResponseDto {

    private List<PostChatDto> postChatList;

    @Builder
    public PostChatResponseDto(List<PostChatDto> postChatList) {
        this.postChatList = postChatList;
    }
}
