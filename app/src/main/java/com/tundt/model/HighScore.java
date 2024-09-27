package com.tundt.model;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by admin on 6/13/2017.
 */

public class HighScore implements Serializable{

    public String place;
    public String name;
    public int score;

    public Bitmap image;

    public HighScore(String place, String name, int score, Bitmap image) {
        this.place = place;
        this.name = name;
        this.score = score;
        this.image = image;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
