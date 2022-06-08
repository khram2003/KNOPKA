package com.hse.knopkabackend.DTO;

import java.io.Serializable;

public class BatchResponseDTO implements Serializable {
    private final String time;
    private final Long authorId;

    public BatchResponseDTO(String time, Long authorId) {
        this.time = time;
        this.authorId = authorId;
    }

    public String getTime() {
        return time;
    }

    public Long getAuthorId() {
        return authorId;
    }
}
