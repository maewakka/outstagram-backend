package com.woo.outstagram.service;

import com.woo.outstagram.dto.follow.UserDto;
import com.woo.outstagram.dto.follow.UserListResponseDto;
import com.woo.outstagram.entity.follow.Follow;
import com.woo.outstagram.entity.user.User;
import com.woo.outstagram.repository.follow.FollowRepository;
import com.woo.outstagram.repository.user.UserRepository;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucket;

    /**
     * Follow할 유저들의 리스트와 Follow상태를 리턴해준다.
     * @param user
     * @return User List
     */
    @Transactional
    public UserListResponseDto getUserList(User user) {
        // DB에서 조회한 모든 유저 리스트
        List<User> userList = userRepository.findAll();

        // UserDto로 변환할 리스트
        List<UserDto> userDtoList = new ArrayList<>();

        // 유저리스트 중 본인을 뺀 나머지 유저들을 UserDto 형식에 맞게 변경
        userList.forEach((u) -> {
            if(!u.getEmail().equals(user.getEmail())) {

                String profileImgUrl = null;

                try {
                    profileImgUrl = minioClient.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                    .method(Method.GET)
                                    .bucket(bucket)
                                    .object(u.getProfileImgUrl())
                                    .expiry(2, TimeUnit.HOURS)
                                    .build());
                } catch (Exception e) {
                    throw  new RuntimeException();
                }


                userDtoList.add(UserDto.builder()
                        .email(u.getEmail())
                        .profileUrl(profileImgUrl)
                        .nickname(u.getNickname())
                        .isFollow(followRepository.existsByFollowerAndFollowing(user, u))
                        .build());
            }
        });

        return UserListResponseDto.builder().userList(userDtoList).build();
    }

    /**
     * Follow 기능
     */
    @Transactional
    public UserListResponseDto saveFollow(User user, String email) {
        // 팔로잉하는 유저 검색
        User following = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("유저 정보를 찾을 수 없습니다."));

        // 팔로잉 유저에 대한 팔로잉 정보 저장
        followRepository.save(Follow.builder()
                        .following(following)
                        .follower(user)
                        .build());

        return this.getUserList(user);
    }

    /**
     * UnFollow 기능
     */
    @Transactional
    public UserListResponseDto deleteFollow(User user, String email) throws Exception {
        // 팔로잉하는 유저와 팔로잉 정보 검색
        User following = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("유저 정보를 찾을 수 없습니다."));
        Follow follow = followRepository.findByFollowerAndFollowing(user, following).orElseThrow(() -> new Exception("팔로우 처리 중 에러가 발생하였습니다"));

        // 팔로잉 정보 삭제
        followRepository.delete(follow);

        return this.getUserList(user);
    }
}
