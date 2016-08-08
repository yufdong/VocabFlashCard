package com.yufandong.vocabflashcard.model;

import java.io.Serializable;

/**
 * A Flashcard with a word on the "front" of the card and a word on the "back". Also contains an ID.
 */
public class Flashcard implements Serializable {
    private long id;
    private String front;
    private String back;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }
}
