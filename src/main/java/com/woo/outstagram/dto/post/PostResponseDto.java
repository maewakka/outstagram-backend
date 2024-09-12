package com.woo.outstagram.dto.post;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PostResponseDto {

    private List<GetPostResp> postList;

    @Builder
    public PostResponseDto(List<GetPostResp> postList) {
        this.postList = postList;
    }
}
