package com.woo.outstagram.entity.user;

import com.woo.outstagram.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "OUTSTAGRAM_USER")
@Data
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "OUTSTAGRAM_USER_SEQUENCE_GENERATOR")
    @SequenceGenerator(name="OUTSTAGRAM_USER_SEQUENCE_GENERATOR", sequenceName = "OUTSTAGRAM_USER_SEQUENCE", initialValue = 1, allocationSize = 1)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;
    private String name;

    private String nickname;
    private String phone;

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    private String introduce;

    private String gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String email, String password, String name, String nickname, String phone, String profileImgUrl, String introduce, String gender, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.profileImgUrl = profileImgUrl;
        this.introduce = introduce;
        this.gender = gender;
        this.role = role;
    }
}
