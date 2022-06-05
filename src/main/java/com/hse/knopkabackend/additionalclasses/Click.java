package com.hse.knopkabackend.additionalclasses;


import javax.persistence.Table;

@Table(name = "entityforclick")
public interface Click {
    Long getClickId();

    Long getClickedKnopkaId();

    String getRegion();

    String getTimeOfClick();
}
