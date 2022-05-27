package com.hse.knopkabackend.additionalclasses;

import javax.persistence.Table;

@Table(name = "tags")
public interface Tag {
    Long getDescriptionId();

    String getTags();


}
