package com.woo.outstagram.service;

import com.woo.exception.util.BizException;
import com.woo.outstagram.dto.follow.UserDto;
import com.woo.outstagram.dto.post.*;
import com.woo.outstagram.dto.post.entity.PostFileDto;
import com.woo.outstagram.entity.post.Post;
import com.woo.outstagram.entity.post.PostChat;
import com.woo.outstagram.entity.post.PostFile;
import com.woo.outstagram.entity.post.PostLike;
import com.woo.outstagram.entity.user.User;
import com.woo.outstagram.mapper.PostMapper;
import com.woo.outstagram.repository.follow.FollowRepository;
import com.woo.outstagram.repository.post.*;
import com.woo.outstagram.util.minio.MinioUtil;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final FollowRepository followRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostChatRepository postChatRepository;
    private final MinioUtil minioUtil;
    private final PostMapper postMapper;

    /**
     * 게시글에 대한 내용(글, 유저, 이미지, 동영상 등)을 저장한다.
     * @param user, UploadPostRequestDto
     */
    @Transactional
    public void uploadPost(User user, UploadPostRequestDto requestDto) {
        List<MultipartFile> files = requestDto.getFile();

        // POST Entity 생성 및 저장
        Post savedPost = Post
                .builder()
                .content(requestDto.getContent())
                .user(user)
                .build();
        postRepository.save(savedPost);

        // POST Image Entity 생성 및 저장
        AtomicInteger index = new AtomicInteger();
        files.stream()
                .forEach((file) -> {
                    String location = "post/" + user.getEmail() + "/" + savedPost.getId() + "/" + file.getOriginalFilename();

                    PostFile savePostFile = PostFile.builder()
                                    .postFileIndex((long) index.getAndIncrement())
                                    .postFileUrl(location)
                                    .post(savedPost)
                                    .user(user)
                                    .build();
                    minioUtil.putObjectToMinio(file, location);
                    postFileRepository.save(savePostFile);
                });
    }

    /**
     * 나와 내가 Follow한 유저들의 게시물들을 반환해준다.
     * @param follower
     */
    @Transactional(readOnly = true)
    public PostResponseDto getPostList(User follower) {
        List<Long> followingUserList = followRepository.followerList(follower).stream().map(User::getId).collect(Collectors.toList());
        followingUserList.add(follower.getId());

        return PostResponseDto.builder()
                .postList(postMapper.getPostList(followingUserList, follower.getId()).stream()
                        .map(postDto -> GetPostResp.of(postDto, minioUtil))
                        .toList())
                .build();
    }

    /**
     * 내가 작성한 게시글의 List를 반환해준다.
     * @param user
     */
    @Transactional(readOnly = true)
    public PostResponseDto getMyPostList(User user) {
        return PostResponseDto.builder()
                .postList(postMapper.getMyPostList(user.getId()).stream()
                        .map(postDto -> GetPostResp.of(postDto, minioUtil))
                        .toList())
                .build();
    }

    /**
     * 게시글을 삭제하는 로직
     * @param user, postId
     */
    public PostResponseDto deletePost(User user, Long postId) {
        deletePostDetail(user, postId);

        return this.getMyPostList(user);
    }

    @Transactional
    public void deletePostDetail(User user, Long postId) {
        Post savedPost = postRepository.findById(postId).orElseThrow(() -> new BizException("post_not_found"));
        List<PostFile> postFileList = postFileRepository.findAllByPost(savedPost);
        List<PostChat> postChatList = postChatRepository.findAllByPostOrderByCreatedDateDesc(savedPost);

        postFileRepository.deleteAll(postFileList);
        postChatRepository.deleteAll(postChatList);
        postRepository.delete(savedPost);
    }


    /**
     * 해당 게시글의 좋아요 눌렀을 때 로직
     * @param user, postId
     */
    public PostResponseDto setPostLike(User user, Long postId) {
        setPostLikeDetail(user, postId);

        return this.getPostList(user);
    }

    @Transactional
    public void setPostLikeDetail(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BizException("post_not_found"));

        // postLike 저장 DB에 요청 좋아요 정보가 없다면, 저장한다.
        if(!postLikeRepository.existsByPostAndUser(post, user)) {
            postLikeRepository.save(PostLike.builder()
                    .post(post)
                    .user(user)
                    .build());
        }
    }

    /**
     * 해당 게시글 좋아요를 취소했을 때 로직
     * @param user, postId
     */
    public PostResponseDto deletePostLike(User user, Long postId) {
        deletePostLikeDetail(user, postId);

        return this.getPostList(user);
    }

    @Transactional
    public void deletePostLikeDetail(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));

        PostLike postLike = postLikeRepository.findByPostAndUser(post, user).orElseThrow(() -> new EntityNotFoundException());

        postLikeRepository.delete(postLike);
    }

    /**
     * 해당 게시글의 댓글들을 모두 반환해주는 로직
     * @param postId
     */
    @Transactional
    public PostChatResponseDto getPostChatList(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BizException("post_not_found"));
        List<PostChat> postChatList = postChatRepository.findAllByPostOrderByCreatedDateDesc(post);

        return PostChatResponseDto.builder().postChatList(
                postChatList.stream().map(postChat -> PostChatDto.of(postChat, minioUtil.getUrlFromMinioObject(postChat.getUser().getProfileImgUrl()))).toList()
        ).build();
    }

    /**
     * 게시글에 댓글을 게시했을 때 저장해주는 로직
     * @param user, PostChatRequestDto
     */
    public PostChatResponseDto setPostChat(User user, PostChatRequestDto requestDto) {
        setPostChatDetail(user, requestDto);

        return this.getPostChatList(requestDto.getPostId());
    }

    @Transactional
    public void setPostChatDetail(User user, PostChatRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));

        postChatRepository.save(PostChat.builder()
                .post(post)
                .user(user)
                .content(requestDto.getContent())
                .build());
    }

    /**
     * 해당 댓글을 삭제하는 로직
     * @param postId, postChatId
     */
    public PostChatResponseDto deletePostChat(Long postId, Long postChatId) {
        deletePostChatDetail(postChatId);

        return this.getPostChatList(postId);
    }

    @Transactional
    public void deletePostChatDetail(Long postChatId) {
        PostChat postChat = postChatRepository.findById(postChatId).orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));

        postChatRepository.delete(postChat);
    }

    /**
     * 게시글의 내요에서 Query 문에 포함된 게시글만 반환해주는 로직
     * @param user, query
     */
    @Transactional
    public PostResponseDto getSearchPostList(User user, String content) {
        return PostResponseDto.builder()
                .postList(postMapper.getSearchPostList(user.getId(), content).stream()
                        .map(postDto -> GetPostResp.of(postDto, minioUtil))
                        .toList())
                .build();
    }
}
