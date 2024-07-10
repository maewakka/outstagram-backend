package com.woo.outstagram.dto.profile;

import lombok.Data;

@Data
public class UpdatePasswordRequestDto {
    private String prevPw;
    private String changePw;
}
