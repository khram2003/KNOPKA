package com.hse.knopkabackend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "knopka_user")
@Table(name = "knopka_user", indexes = @Index(name = "unique_email_indexes", columnList = "email", unique = true))
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
            unique = true,
            columnDefinition = "VARCHAR(2048)"
    )
    private String email;

    @Column(
            name = "token",
            unique = true,
            columnDefinition = "VARCHAR(2048)"
    )
    private String token;

    @Column(
            name = "friends",
            updatable = true
    )
    @ElementCollection

    private Set<Long> friends;


    @ElementCollection
    @CollectionTable(name = "knopkaIds", joinColumns = @JoinColumn(name = "id"))
    @Column(
            name = "knopkaIds"
    )
    Set<Long> knopkaIds = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    private Profile profile;

    @OneToMany(mappedBy = "user")
    Set<Knopka> knopkas;

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

    public KnopkaUser(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public Set<Knopka> getKnopkas() {
        return knopkas;
    }

    public void setKnopkas(Set<Knopka> knopkas) {
        this.knopkas = knopkas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long curId) {
        id = curId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public Set<Long> getFriends() {
        return friends;
    }

    public Set<Long> getKnopkaIds() {
        return knopkaIds;
    }

    public void setKnopkaIds(Set<Long> knopkaIds) {
        this.knopkaIds = knopkaIds;
    }


    public void setFriends(Set<Long> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "KnopkaUser{" +
                "id=" + id +
                ", email='" + email +
                "'}";
    }
}
