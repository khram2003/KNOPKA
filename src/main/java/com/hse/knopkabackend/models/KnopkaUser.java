package com.hse.knopkabackend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity(name = "knopka_user")
@Table(name = "knopka_user")
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
            name = "email",
            nullable = false,
            updatable = true,
            unique = true,
            columnDefinition = "TEXT"
    )
    private String email;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    private Profile profile;

    @OneToMany(mappedBy = "user")
    List<Knopka> knopkas;

    public KnopkaUser(Long curId, Profile curProfile) {
        id = curId;
        profile = curProfile;
    }

    public KnopkaUser(Profile profile) {
        this.profile = profile;
    }

    public KnopkaUser(String email) {
        this.email = email;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public KnopkaUser() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long curId) {
        id = curId;
    }


    @Override
    public String toString() {
        return "KnopkaUser{" +
                "id=" + id +
                ", email='" + email +
                "'}";
    }
}
