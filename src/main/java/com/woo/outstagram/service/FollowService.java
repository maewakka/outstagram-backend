package com.woo.outstagram.service;

import com.woo.exception.util.BizException;
import com.woo.outstagram.dto.follow.UserDto;
import com.woo.outstagram.dto.follow.UserListResponseDto;
import com.woo.outstagram.entity.follow.Follow;
import com.woo.outstagram.entity.user.User;
import com.woo.outstagram.mapper.FollowMapper;
import com.woo.outstagram.repository.follow.FollowRepository;
import com.woo.outstagram.repository.user.UserRepository;
import com.woo.outstagram.util.minio.MinioUtil;
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
    private final MinioUtil minioUtil;
    private final FollowMapper followMapper;

    /**
     * Follow할 유저들의 리스트와 Follow상태를 리턴해준다.
     * @param own
     * @return User List
     */
    @Transactional
    public UserListResponseDto getUserList(User own) {
        List<UserDto> userDtoList = followMapper.getUserLists(own.getId());
        userDtoList.stream().forEach(userDto -> userDto.setProfileUrl(minioUtil.getUrlFromMinioObject(userDto.getProfileUrl())));

        return UserListResponseDto
                .builder()
                .userList(userDtoList)
                .build();
    }

    /**
     * Follow 기능
     */
    public UserListResponseDto saveFollow(User user, String email) {
        saveFollowDetail(user, email);

        return this.getUserList(user);
    }

    @Transactional
    public void saveFollowDetail(User user, String email) {
        // 팔로잉하는 유저 검색
        User following = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("유저 정보를 찾을 수 없습니다."));

        // 팔로잉 유저에 대한 팔로잉 정보 저장
        followRepository.save(Follow.builder()
                .following(following)
                .follower(user)
                .build());
    }

    /**
     * UnFollow 기능
     */
    public UserListResponseDto deleteFollow(User user, String email) {
        deleteFollowDetail(user, email);

        return this.getUserList(user);
    }

    @Transactional
    public void deleteFollowDetail(User user, String email) {
        // 팔로잉하는 유저와 팔로잉 정보 검색
        User following = userRepository.findByEmail(email).orElseThrow(() -> new BizException("user_not_found"));
        Follow follow = followRepository.findByFollowerAndFollowing(user, following).orElseThrow(() -> new BizException("follow_error"));

        // 팔로잉 정보 삭제
        followRepository.delete(follow);
    }
}
