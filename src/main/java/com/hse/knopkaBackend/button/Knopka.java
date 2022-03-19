package com.hse.knopkaBackend.button;

public class Knopka {
    String text;
    Long authorId;

    public Knopka(String curText, Long curAuthorId) {
        text = curText;
        authorId = curAuthorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "Knopka{" +
                "text='" + text + '\'' +
                ", authorId=" + authorId +
                '}';
    }
}
