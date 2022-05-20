package com.hse.knopkabackend.models.entityforclick;

import com.hse.knopkabackend.additionalclasses.Style;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "entityforclick")
public class EntityForClick {
    @Id
    @SequenceGenerator(
            name = "click_sequence",
            sequenceName = "click_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "click_sequence"
    )
    @Column(
            name = "click_id",
            updatable = false,
            nullable = false
    )
    private Long clickId;

    @Column(
            name = "clicked_knopka_id"
    )
    Long clickedKnopkaId;

    public EntityForClick() {
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
