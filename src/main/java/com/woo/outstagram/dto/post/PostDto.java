package com.woo.outstagram.dto.post;

import com.woo.outstagram.dto.follow.UserDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PostDto {

    private Long postId;
    private UserDto user;
    private String content;
    private List<PostFileDto> postFileList;
    private boolean like;
    private Long countLike;
    private Long countChat;

    @Builder
    public PostDto(Long postId, UserDto user, String content, List<PostFileDto> postFileList, boolean like, Long countLike, Long countChat) {
        this.postId = postId;
        this.user = user;
        this.content = content;
        this.postFileList = postFileList;
        this.like = like;
        this.countLike = countLike;
        this.countChat = countChat;
    }
}
