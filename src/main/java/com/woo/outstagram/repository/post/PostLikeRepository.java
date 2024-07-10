package com.woo.outstagram.repository.post;

import com.woo.outstagram.entity.post.Post;
import com.woo.outstagram.entity.post.PostLike;
import com.woo.outstagram.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostAndUser(Post post, User user);
    boolean existsByPostAndUser(Post post, User user);
    Long countByPost(Post post);

}