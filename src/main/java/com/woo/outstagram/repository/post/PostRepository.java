package com.woo.outstagram.repository.post;

import com.woo.outstagram.entity.post.Post;
import com.woo.outstagram.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserOrderByModifiedDateDesc(User user);
    Long countByUser(User user);
    @Query("SELECT p FROM Post p WHERE p.user IN :userList ORDER BY p.modifiedDate DESC")
    List<Post> getPosts(@Param("userList") List<User> userList);

    @Query("SELECT p FROM Post p WHERE p.content LIKE :content ORDER BY p.modifiedDate desc")
    List<Post> getPosts(@Param("content") String content);
}
