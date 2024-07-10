package com.woo.outstagram.repository.follow;

import com.woo.outstagram.entity.follow.Follow;
import com.woo.outstagram.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowing(User follower, User following);
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
    Long countByFollower(User user);
    Long countByFollowing(User user);
    @Query("SELECT f.following FROM Follow f WHERE f.follower = :follower ")
    List<User> followerList(@Param("follower") User follower);
}
