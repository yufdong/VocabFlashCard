package com.yufandong.vocabflashcard.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YuFan on 3/29/16.
 */
public class VocabSet implements Serializable{

    private List<Word> list;
    private String name;
    private long id;

    public VocabSet() {
        list = new ArrayList<>();
    }

    public List<Word> getList() {
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
