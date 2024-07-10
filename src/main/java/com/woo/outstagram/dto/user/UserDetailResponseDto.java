package com.woo.outstagram.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
public class UserDetailResponseDto {

    private String email;
    private String nickname;
    private String phone;
    private String profileUrl;
    private String introduce;
    private String gender;

    @Builder
    public UserDetailResponseDto(String email, String nickname, String phone, String profileUrl, String introduce, String gender) {
        this.email = email;
        this.nickname = nickname;
        this.phone = phone;
        this.profileUrl = profileUrl;
        this.introduce = introduce;
        this.gender = gender;
    }
}
