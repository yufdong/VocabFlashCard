package com.yufandong.vocabflashcard.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for a vocab set. Contains an ID, name and a list of Flashcard.
 */
public class VocabSet implements Serializable{

    private List<Flashcard> list;
    private String name;
    private long id;

    public VocabSet() {
        list = new ArrayList<>();
    }

    public List<Flashcard> getList() {
        return list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
