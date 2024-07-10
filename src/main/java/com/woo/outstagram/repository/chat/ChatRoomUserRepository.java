package com.woo.outstagram.repository.chat;

import com.woo.outstagram.entity.chat.ChatRoomUser;
import com.woo.outstagram.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    Optional<ChatRoomUser> findByUserAndTargetUser(User user, User targetUser);
}
