package com.hse.knopkabackend.additionalclasses;

import java.io.Serializable;

public class Style implements Serializable {
    String color;

    public Style() {
    }

    public Style(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
