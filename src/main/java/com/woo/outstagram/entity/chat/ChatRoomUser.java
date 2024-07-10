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
public class ChatRoomUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "CHAT_ROOM_USER_SEQUENCE_GENERATOR")
    @SequenceGenerator(name="CHAT_ROOM_USER_SEQUENCE_GENERATOR", sequenceName = "CHAT_ROOM_USER_SEQUENCE", initialValue = 1, allocationSize = 1)
    @Column(name = "chat_room_user_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    @Builder
    public ChatRoomUser(ChatRoom chatRoom, User user, User targetUser) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.targetUser = targetUser;
    }
}
