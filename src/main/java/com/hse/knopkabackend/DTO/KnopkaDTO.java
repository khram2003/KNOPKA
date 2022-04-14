package com.hse.knopkabackend.DTO;

import com.hse.knopkabackend.additionalclasses.Style;

import java.io.Serializable;

public class KnopkaDTO implements Serializable {
    private String name;
    private Style style;
    private Long pushes;
    private Long id;

    KnopkaDTO() {
    }

    public KnopkaDTO(String name, Style style, Long pushes, Long id) {
        this.name = name;
        this.style = style;
        this.pushes = pushes;
        this.id = id;
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
}
