package com.hse.knopkaBackend.knopkaUser;

import javax.persistence.*;

@Entity
@Table
public class KnopkaUser {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    private String nickname;


    public KnopkaUser(Long curId, String curNickname) {
        id = curId;
        nickname = curNickname;
    }

    public KnopkaUser(String nickname) {
        this.nickname = nickname;
    }

    public KnopkaUser() {

    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }


    public void setId(Long curId) {
        id = curId;
    }

    public void setNickname(String curNickname) {
        nickname = curNickname;
    }


    @Override
    public String toString() {
        return "KnopkaUser{" +
                "id=" + id +
                ", nickname='" + nickname +
                "'}";
    }
}
