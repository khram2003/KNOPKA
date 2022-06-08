package com.hse.knopkabackend.DTO;

import java.io.Serializable;

public class BatchDTO implements Serializable {
    private String time;
    private Long pushes;
    private String region;
    private Long clickedKnopkaId;
    private Long authorId;

    public BatchDTO() {
    }

    public BatchDTO(String time, Long pushes, String region) {
        this.time = time;
        this.pushes = pushes;
        this.region = region;
    }

    public Long getClickedKnopkaId() {
        return clickedKnopkaId;
    }

    public void setClickedKnopkaId(Long clickedKnopkaId) {
        this.clickedKnopkaId = clickedKnopkaId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getPushes() {
        return pushes;
    }

    public void setPushes(Long pushes) {
        this.pushes = pushes;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
