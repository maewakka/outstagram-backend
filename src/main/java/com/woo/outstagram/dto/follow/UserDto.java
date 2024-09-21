package com.woo.outstagram.dto.follow;

import com.woo.outstagram.entity.user.User;
import com.woo.outstagram.util.minio.MinioUtil;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String email;
    private String nickname;
    private String profileUrl;
    private boolean isFollow;


    public static UserDto of(User user, MinioUtil minioUtil) {
        return UserDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileUrl(minioUtil.getUrlFromMinioObject(user.getProfileImgUrl()))
                .build();
    }
}
