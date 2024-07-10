package com.woo.outstagram.dto.follow;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class UserListResponseDto {

    private List<UserDto> userList;

    @Builder
    public UserListResponseDto(List<UserDto> userList) {
        this.userList = userList;
    }
}
