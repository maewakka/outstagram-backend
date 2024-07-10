package com.woo.outstagram.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class ProfileUpdateRequestDto {
    private String nickname;
    private String introduce;

    @NotBlank(message = "빈 항목이 존재합니다.")
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}", message = "핸드폰 번호 형식이 잘못 되었습니다.")
    private String phone;

    private String gender;
}
