package com.woo.outstagram.dto.profile;

import lombok.Builder;
import lombok.Data;

@Data
public class ProfileCountResponseDto {

    private Long board;
    private Long follower;
    private Long follow;

    @Builder
    public ProfileCountResponseDto(Long board, Long follower, Long follow) {
        this.board = board;
        this.follower = follower;
        this.follow = follow;
    }
}
