package com.woo.outstagram.entity.chat;

import com.woo.outstagram.entity.BaseTimeEntity;
import com.woo.outstagram.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@NoArgsConstructor
public class Chat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "CHAT_SEQUENCE_GENERATOR")
    @SequenceGenerator(name="CHAT_SEQUENCE_GENERATOR", sequenceName = "CHAT_SEQUENCE", initialValue = 1, allocationSize = 1)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private String content;

    @Builder
    public Chat(User user, ChatRoom chatRoom, String content) {
        this.user = user;
        this.chatRoom = chatRoom;
        this.content = content;
    }
}
