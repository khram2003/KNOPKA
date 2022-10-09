package com.hse.knopkabackend.additionalclasses;


import javax.persistence.Table;

@Table(name = "entityforbatch")
public interface Click {
    Long getAuthorId();

    Long getClickedKnopkaId();

    Long getPushes();

    String getRegion();

    String getTimeOfClick();
}
