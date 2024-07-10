package com.woo.outstagram.entity.follow;

import com.woo.outstagram.entity.BaseTimeEntity;
import com.woo.outstagram.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@NoArgsConstructor
public class Follow extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "FOLLOW_SEQUENCE_GENERATOR")
    @SequenceGenerator(name="FOLLOW_SEQUENCE_GENERATOR", sequenceName = "FOLLOW_SEQUENCE", initialValue = 1, allocationSize = 1)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following")
    private User following;

    @Builder
    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }
}
