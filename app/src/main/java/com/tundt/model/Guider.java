package com.tundt.model;

/**
 * Created by admin on 6/26/2017.
 */

public class Guider {
    public String text;
    public int resource;

    public Guider(String text, int resource) {
        this.text = text;
        this.resource = resource;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
