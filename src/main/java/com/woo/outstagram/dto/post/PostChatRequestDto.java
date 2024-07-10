package com.woo.outstagram.dto.post;

import lombok.Builder;
import lombok.Data;

@Data
public class PostChatRequestDto {

    private Long postId;
    private String content;

    @Builder
    public PostChatRequestDto(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }
}
