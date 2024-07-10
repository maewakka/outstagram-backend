package com.woo.outstagram.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponseDto {
    private String accessToken;

    @Builder
    public LoginResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
