package com.woo.outstagram.dto.post;

import com.woo.outstagram.entity.post.PostFile;
import lombok.Builder;
import lombok.Data;

@Data
public class PostFileDto {

    private Long fileOrder;
    private String fileUrl;

    @Builder
    public PostFileDto(Long fileOrder, String fileUrl) {
        this.fileOrder = fileOrder;
        this.fileUrl = fileUrl;
    }

    public static PostFileDto toDto(PostFile postFile) {
        return PostFileDto.builder()
                .fileOrder(postFile.getPostFileIndex())
                .fileUrl(postFile.getPostFileUrl())
                .build();
    }
}
