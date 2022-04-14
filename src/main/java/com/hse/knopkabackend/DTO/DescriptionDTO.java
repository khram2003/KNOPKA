package com.hse.knopkabackend.DTO;

import java.io.Serializable;
import java.util.List;

public class DescriptionDTO implements Serializable {
    private String text;
    private byte[] image;
    private List<String> tags;

    public DescriptionDTO() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
