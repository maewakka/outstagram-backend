package com.woo.outstagram.repository.chat;

import com.woo.outstagram.entity.chat.Chat;
import com.woo.outstagram.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c WHERE c.chatRoom = :chatRoom ORDER BY c.createdDate")
    List<Chat> findAllByChatRoom(ChatRoom chatRoom);
}
