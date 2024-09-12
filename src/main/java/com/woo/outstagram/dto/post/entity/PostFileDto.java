package com.woo.outstagram.dto.post.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostFileDto {
    private Long postFileId;
    private Long postFileIndex;
    private String postFileUrl;
}
