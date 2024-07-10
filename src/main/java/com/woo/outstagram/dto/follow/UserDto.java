package com.woo.outstagram.dto.follow;

import com.woo.outstagram.entity.user.User;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Data
public class UserDto {

    private String email;
    private String nickname;
    private String profileUrl;
    private boolean isFollow;

    @Builder
    public UserDto(String email, String nickname, String profileUrl, boolean isFollow) {
        this.email = email;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.isFollow = isFollow;
    }

    public static UserDto toDto(User user, MinioClient minioClient) {
        Map<String, String> reqParams = new HashMap<String, String>();
        reqParams.put("response-content-type", "image/jpeg");
        String profileImgUrl = null;

        String url = null;
        try {
            url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket("outstagram")
                            .object(user.getProfileImgUrl())
                            .expiry(2, TimeUnit.HOURS)
                            .extraQueryParams(reqParams)
                            .build());
        } catch (Exception e) {
            throw  new RuntimeException();
        }

        return UserDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileUrl(url)
                .build();
    }
}
