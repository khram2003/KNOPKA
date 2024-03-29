package com.hse.knopkabackend.models.entityforclick;

import javax.persistence.*;

@Entity(name = "entityforclick")
public class EntityForClick {
    @Id
    @Column(
            name = "click_id",
            updatable = false
    )
    private Long clickId;

    @Column(
            name = "clicked_knopka_id"
    )
    private Long clickedKnopkaId;

    @Column(
            name = "time_of_click"
    )
    private String time;

    @Column(
            name = "region"
    )
    private String region;


    public EntityForClick() {
    }

    public EntityForClick(Long clickId) {
        this.clickId = clickId;
    }

    public Long getClickId() {
        return clickId;
    }

    public void setClickId(Long clickId) {
        this.clickId = clickId;
    }

    public Long getClickedKnopkaId() {
        return clickedKnopkaId;
    }

    public void setClickedKnopkaId(Long clickedKnopkaId) {
        this.clickedKnopkaId = clickedKnopkaId;
    }
}
