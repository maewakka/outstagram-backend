package com.woo.outstagram.dto.post;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PostResponseDto {

    private List<PostDto> postList;

    @Builder
    public PostResponseDto(List<PostDto> postList) {
        this.postList = postList;
    }
}
