package com.yufandong.vocabflashcard.model;

import com.yufandong.vocabflashcard.model.Word;

/**
 * Created by YuFan on 3/29/16.
 */
public class WordCard {
    private final Word word;
    private boolean isReveal;
    private boolean isEdit;

    public WordCard(Word word) {
        this.word = word;
        isReveal = false;
        isEdit = false;
    }

    public Word getWord() {
        return word;
    }

    public boolean isReveal() {
        return isReveal;
    }

    public void setReveal(boolean reveal) {
        isReveal = reveal;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
