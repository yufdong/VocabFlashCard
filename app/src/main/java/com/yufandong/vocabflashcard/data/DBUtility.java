package com.yufandong.vocabflashcard.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yufandong.vocabflashcard.model.VocabSet;
import com.yufandong.vocabflashcard.model.Word;

import java.util.ArrayList;
import java.util.List;

public class DBUtility {

    private VocabDatabase dbHelper;

    public DBUtility(Context context) {
        dbHelper = new VocabDatabase(context);
    }

    public long addVocabSet(VocabSet set) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues setValues = new ContentValues();
        setValues.put(VocabDatabase.Set.NAME, set.getName());

        long newId = db.insert(VocabDatabase.Set.TABLE_NAME, null, setValues);

        for(Word word : set.getList()) {
            ContentValues wordValues = new ContentValues();
            wordValues.put(VocabDatabase.Word.SET_ID, newId);
            wordValues.put(VocabDatabase.Word.FRONT, word.getFront());
            wordValues.put(VocabDatabase.Word.BACK, word.getBack());

            db.insert(VocabDatabase.Word.TABLE_NAME, null, wordValues);
        }

        return newId;
    }

    public VocabSet getVocabSet(long id) {
        VocabSet vocabSet = new VocabSet();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] setProj = { VocabDatabase.Set.NAME };
        Cursor setCur = db.query(
                VocabDatabase.Set.TABLE_NAME,
                setProj,
                VocabDatabase.Set._ID + "=" + id,
                null,
                null,
                null,
                null
        );

        if (setCur.moveToFirst()) {
            vocabSet.setId(id);
            vocabSet.setName(setCur.getString(setCur.getColumnIndexOrThrow(VocabDatabase.Set.NAME)));
            setCur.close();
        }
        else {
            setCur.close();
            return null;
        }

        String[] wordProj = { VocabDatabase.Word._ID, VocabDatabase.Word.FRONT, VocabDatabase.Word.BACK };
        Cursor wordCur = db.query(
                VocabDatabase.Word.TABLE_NAME,
                wordProj,
                VocabDatabase.Word.SET_ID + "=" + id,
                null,
                null,
                null,
                null
        );

        while(wordCur.moveToNext()) {
            long wordId = wordCur.getLong(wordCur.getColumnIndexOrThrow(VocabDatabase.Word._ID));
            String front = wordCur.getString(wordCur.getColumnIndexOrThrow(VocabDatabase.Word.FRONT));
            String back = wordCur.getString(wordCur.getColumnIndexOrThrow(VocabDatabase.Word.BACK));
            Word word = new Word();
            word.setId(wordId);
            word.setFront(front);
            word.setBack(back);
            vocabSet.getList().add(word);
        }

        wordCur.close();
        return vocabSet;
    }

    public List<VocabSet> getAllVocabSet() {
        List<VocabSet> setList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] setProj = { VocabDatabase.Set._ID, VocabDatabase.Set.NAME };
        Cursor setCur = db.query(
                VocabDatabase.Set.TABLE_NAME,
                setProj,
                null,
                null,
                null,
                null,
                null
        );

        while (setCur.moveToNext()) {
            long id = setCur.getInt(setCur.getColumnIndexOrThrow(VocabDatabase.Set._ID));
            String name = setCur.getString(setCur.getColumnIndexOrThrow(VocabDatabase.Set.NAME));
            VocabSet vocabSet = new VocabSet();
            vocabSet.setId(id);
            vocabSet.setName(name);
            setList.add(vocabSet);
        }

        setCur.close();
        return setList;
    }

    public long updateWord(Word word) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues wordValues = new ContentValues();
        wordValues.put(VocabDatabase.Word.FRONT, word.getFront());
        wordValues.put(VocabDatabase.Word.BACK, word.getBack());

        return db.update(VocabDatabase.Word.TABLE_NAME, wordValues, VocabDatabase.Word._ID + "=" + word.getId(), null);
    }

    public void resetDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.resetDatabase(db);
    }
}
