package com.woo.outstagram.repository.post;

import com.woo.outstagram.entity.post.Post;
import com.woo.outstagram.entity.post.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {
    List<PostFile> findAllByPost(Post post);
}
