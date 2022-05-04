package com.hse.knopkabackend.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hse.knopkabackend.additionalclasses.Style;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "knopka")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Table(name = "knopka")
public class Knopka {
    @Id
    @SequenceGenerator(
            name = "knopka_sequence",
            sequenceName = "knopka_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "knopka_sequence"
    )
    @Column(
            name = "knopka_id",
            updatable = false,
            nullable = false
    )
    private Long knopkaId;

    @Column(
            name = "knopka_name",
            nullable = false,
            columnDefinition = "VARCHAR(256)"
    )
    String name;

    @Column(
            name = "pushes"
    )
    private Long pushesCounter = 0L;

    @Type(type = "jsonb")
    @Column(
            name = "style",
            columnDefinition = "jsonb"
    )
    private Style style;

    @Column(
            name = "createdAt"
    )
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "knopka", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    private Description description;

    @ManyToOne
    @JoinColumn(name = "id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    KnopkaUser user;

    public Knopka(String name, Long pushes, Style style) {
        this.name = name;
        this.pushesCounter = pushes;
        this.style = style;
    }

    public KnopkaUser getUser() {
        return user;
    }

    public Long getKnopkaId() {
        return knopkaId;
    }

    public void setKnopkaId(Long knopkaId) {
        this.knopkaId = knopkaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPushesCounter() {
        return pushesCounter;
    }

    public void setPushesCounter(Long pushesCounter) {
        this.pushesCounter = pushesCounter;
    }

    public void setUser(KnopkaUser user) {
        this.user = user;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public Knopka() {

    }

    public Knopka(Long knopkaId, String name, Long pushesCounter, Style style, LocalDateTime createdAt, Description description, KnopkaUser user) {
        this.knopkaId = knopkaId;
        this.name = name;
        this.pushesCounter = pushesCounter;
        this.style = style;
        this.createdAt = createdAt;
        this.description = description;
        this.user = user;
    }
}
