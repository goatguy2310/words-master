package com.tundt.model;

import java.io.Serializable;

public class Note implements Serializable {
    public String word;
    public String meaning;

    private int id;

    public boolean showing = false;

    public Note() {
    }

    public Note(String word, String meaning, int id) {
        this.word = word;
        this.meaning = meaning;
        this.setId(id);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String toString() {
        return getWord() + "|" + getMeaning();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
