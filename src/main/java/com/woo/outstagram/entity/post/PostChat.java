package com.woo.outstagram.entity.post;

import com.woo.outstagram.entity.BaseTimeEntity;
import com.woo.outstagram.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@NoArgsConstructor
public class PostChat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "POST_CHAT_SEQUENCE_GENERATOR")
    @SequenceGenerator(name="POST_CHAT_SEQUENCE_GENERATOR", sequenceName = "POST_CHAT_SEQUENCE", initialValue = 1, allocationSize = 1)
    @Column(name = "post_chat_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    @Builder
    public PostChat(Post post, User user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;
    }
}
