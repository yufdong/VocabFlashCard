package com.yufandong.vocabflashcard.model;

import java.io.Serializable;

/**
 * Created by YuFan on 3/29/16.
 */
public class Word implements Serializable {
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
