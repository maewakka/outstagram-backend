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
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "CHAT_ROOM_SEQUENCE_GENERATOR")
    @SequenceGenerator(name="CHAT_ROOM_SEQUENCE_GENERATOR", sequenceName = "CHAT_ROOM_SEQUENCE", initialValue = 1, allocationSize = 1)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "create_user_id")
    private User user;

    private String lastMessage;

    @Builder
    public ChatRoom(User user, String lastMessage) {
        this.user = user;
        this.lastMessage = lastMessage;
    }
}
