package com.hse.knopkabackend.DTO;

import com.hse.knopkabackend.additionalclasses.Style;

import java.io.Serializable;
import java.time.LocalDateTime;

public class KnopkaDTO implements Serializable {
    private String name;
    private Style style;
    private Long pushes;
    private Long id;
//    private LocalDateTime createdAt;

    KnopkaDTO() {
    }

    public KnopkaDTO(String name, Style style, Long pushes, Long id/*, LocalDateTime createdAt*/) {
        this.name = name;
        this.style = style;
        this.pushes = pushes;
        this.id = id;
//        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Long getPushes() {
        return pushes;
    }

    public void setPushes(Long pushes) {
        this.pushes = pushes;
    }

//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }

//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
}
