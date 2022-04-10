package com.hse.knopkabackend.models;


import javax.persistence.*;

@Entity(name = "knopka")
@Table(name = "knopka")
public class Knopka {
    @Id
    @Column(
            name = "knopka_id",
            updatable = false
    )
    private Long knopkaId;

    @Column(
            name = "knpka_name",
            nullable = false,
            columnDefinition = "VARCHAR(256)"
    )
    String name;

    @Column(
            name = "profile_user_id"
    )
    private Long pushesCounter = 0L;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    KnopkaUser user;

    public KnopkaUser getUser() {
        return user;
    }



    public Knopka() {

    }



}
