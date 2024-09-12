package com.woo.outstagram.dto.post.entity;

import com.woo.outstagram.entity.user.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Long postId;
    private String email;
    private String nickname;
    private String profileImgUrl;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long chatCount;
    private Long likeCount;
    private Boolean isLiked;
    private List<PostFileDto> postFileDtoList;

}
