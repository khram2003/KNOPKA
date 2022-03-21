package com.hse.knopkabackend.models;

import javax.persistence.*;

@Entity(name = "knopka_user")
@Table(
        name = "knopka_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_nickname_unique", columnNames = "nickname")
        }
)
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
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;
    @Column(
            name = "nickname",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String nickname;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Profile profile;

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
