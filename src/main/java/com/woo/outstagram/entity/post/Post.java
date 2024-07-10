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
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "POST_SEQUENCE_GENERATOR")
    @SequenceGenerator(name="POST_SEQUENCE_GENERATOR", sequenceName = "POST_SEQUENCE", initialValue = 1, allocationSize = 1)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "made_user")
    private User user;

    private String content;

    @Builder
    public Post(User user, String content) {
        this.user = user;
        this.content = content;
    }
}
