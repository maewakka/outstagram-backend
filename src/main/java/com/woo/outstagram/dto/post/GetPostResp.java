package com.woo.outstagram.dto.post;

import com.woo.outstagram.dto.follow.UserDto;
import com.woo.outstagram.dto.post.entity.PostDto;
import com.woo.outstagram.dto.post.entity.PostFileDto;
import com.woo.outstagram.util.minio.MinioUtil;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class GetPostResp {

    private Long postId;
    private UserDto user;
    private String content;
    private List<GetPostFileResp> postFileList;
    private boolean like;
    private Long countLike;
    private Long countChat;

    @Builder
    public GetPostResp(Long postId, UserDto user, String content, List<GetPostFileResp> postFileList, boolean like, Long countLike, Long countChat) {
        this.postId = postId;
        this.user = user;
        this.content = content;
        this.postFileList = postFileList;
        this.like = like;
        this.countLike = countLike;
        this.countChat = countChat;
    }

    public static GetPostResp of(PostDto postDto, MinioUtil minioUtil) {
        return GetPostResp.builder()
                .postId(postDto.getPostId())
                .postFileList(postDto.getPostFileDtoList().stream().map(postFileDto -> GetPostFileResp.of(postFileDto, minioUtil)).toList())
                .user(UserDto.builder()
                        .email(postDto.getEmail())
                        .nickname(postDto.getNickname())
                        .profileUrl(minioUtil.getUrlFromMinioObject(postDto.getProfileImgUrl()))
                        .build())
                .content(postDto.getContent())
                .countChat(postDto.getChatCount())
                .countLike(postDto.getLikeCount())
                .like(postDto.getIsLiked())
                .build();
    }

    @Data
    @Builder
    public static class GetPostFileResp {
        private Long fileOrder;
        private String fileUrl;

        public static GetPostFileResp of(PostFileDto postFileDto, MinioUtil minioUtil) {
            return GetPostFileResp.builder()
                    .fileOrder(postFileDto.getPostFileIndex())
                    .fileUrl(minioUtil.getUrlFromMinioObject(postFileDto.getPostFileUrl()))
                    .build();
        }
    }
}
