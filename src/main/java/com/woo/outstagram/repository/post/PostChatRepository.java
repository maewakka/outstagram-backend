package com.woo.outstagram.repository.post;

import com.woo.outstagram.entity.post.Post;
import com.woo.outstagram.entity.post.PostChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostChatRepository extends JpaRepository<PostChat, Long> {
    List<PostChat> findAllByPostOrderByCreatedDateDesc(Post post);
    Long countByPost(Post post);
}
