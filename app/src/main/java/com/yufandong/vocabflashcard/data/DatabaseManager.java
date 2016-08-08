package com.yufandong.vocabflashcard.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yufandong.vocabflashcard.exception.DatabaseInsertException;
import com.yufandong.vocabflashcard.exception.DatabaseUpdateException;
import com.yufandong.vocabflashcard.model.VocabSet;
import com.yufandong.vocabflashcard.model.Flashcard;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains common method to help get and set data in the database.
 */
public class DatabaseManager {

    private SqliteDatabase dbHelper;

    public DatabaseManager(Context context) {
        dbHelper = new SqliteDatabase(context);
    }

    /**
     * Add a new vocab set and the flashcards it contains to the database
     */
    public long addVocabSet(VocabSet set) throws DatabaseInsertException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues setValues = new ContentValues();
        setValues.put(SqliteDatabase.VocabSetTable.NAME, set.getName());

        long setId = db.insert(SqliteDatabase.VocabSetTable.TABLE_NAME, null, setValues);
        if (setId == -1) {
            throw new DatabaseInsertException();
        }

        for (Flashcard flashcard : set.getList()) {
            ContentValues flashcardValues = new ContentValues();
            flashcardValues.put(SqliteDatabase.FlashcardTable.SET_ID, setId);
            flashcardValues.put(SqliteDatabase.FlashcardTable.FRONT, flashcard.getFront());
            flashcardValues.put(SqliteDatabase.FlashcardTable.BACK, flashcard.getBack());

            long flashcardId = db.insert(SqliteDatabase.FlashcardTable.TABLE_NAME, null, flashcardValues);
            if (flashcardId == -1) {
                throw new DatabaseInsertException();
            }
        }

        return setId;
    }

    /**
     * Get a vocab set and the flashcards it contains from the database based on its ID
     */
    public VocabSet getVocabSet(long id) {
        VocabSet vocabSet = new VocabSet();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] vocabSetProjection = {SqliteDatabase.VocabSetTable.NAME};
        Cursor vocabSetCursor = db.query(
                SqliteDatabase.VocabSetTable.TABLE_NAME,
                vocabSetProjection,
                SqliteDatabase.VocabSetTable._ID + "=" + id,
                null, null, null, null);

        if (vocabSetCursor.moveToFirst()) {
            vocabSet.setId(id);
            vocabSet.setName(vocabSetCursor.getString(vocabSetCursor.getColumnIndexOrThrow(SqliteDatabase
                    .VocabSetTable.NAME)));
            vocabSetCursor.close();
        }
        else {
            vocabSetCursor.close();
            return null;
        }

        String[] flashcardProjection = {SqliteDatabase.FlashcardTable._ID, SqliteDatabase.FlashcardTable.FRONT, SqliteDatabase.FlashcardTable.BACK};
        Cursor flashcardCursor = db.query(
                SqliteDatabase.FlashcardTable.TABLE_NAME,
                flashcardProjection,
                SqliteDatabase.FlashcardTable.SET_ID + "=" + id,
                null, null, null, null);

        while (flashcardCursor.moveToNext()) {
            long flashcardId = flashcardCursor.getLong(flashcardCursor.getColumnIndexOrThrow(SqliteDatabase
                    .FlashcardTable
                    ._ID));
            String front = flashcardCursor.getString(flashcardCursor.getColumnIndexOrThrow(SqliteDatabase.FlashcardTable
                    .FRONT));
            String back = flashcardCursor.getString(flashcardCursor.getColumnIndexOrThrow(SqliteDatabase.FlashcardTable
                    .BACK));
            Flashcard flashcard = new Flashcard();
            flashcard.setId(flashcardId);
            flashcard.setFront(front);
            flashcard.setBack(back);
            vocabSet.getList().add(flashcard);
        }

        flashcardCursor.close();
        return vocabSet;
    }

    /**
     * Get all the vocab sets from the database, without the flashcards they contain. (Only has vocab set name and ID)
     */
    public List<VocabSet> getAllVocabSets() {
        List<VocabSet> setList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] vocabSetProjection = {SqliteDatabase.VocabSetTable._ID, SqliteDatabase.VocabSetTable.NAME};
        Cursor setCur = db.query(
                SqliteDatabase.VocabSetTable.TABLE_NAME,
                vocabSetProjection,
                null, null, null, null, null);

        while (setCur.moveToNext()) {
            long id = setCur.getInt(setCur.getColumnIndexOrThrow(SqliteDatabase.VocabSetTable._ID));
            String name = setCur.getString(setCur.getColumnIndexOrThrow(SqliteDatabase.VocabSetTable.NAME));
            VocabSet vocabSet = new VocabSet();
            vocabSet.setId(id);
            vocabSet.setName(name);
            setList.add(vocabSet);
        }

        setCur.close();
        return setList;
    }

    /**
     * Update an existing flashcards in the database.
     */
    public long updateFlashCard(Flashcard flashcard) throws DatabaseUpdateException {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues flashcardValues = new ContentValues();
        flashcardValues.put(SqliteDatabase.FlashcardTable.FRONT, flashcard.getFront());
        flashcardValues.put(SqliteDatabase.FlashcardTable.BACK, flashcard.getBack());

        long numOfRowsUpdated = db.update(
                SqliteDatabase.FlashcardTable.TABLE_NAME,
                flashcardValues,
                SqliteDatabase.FlashcardTable._ID + "=" + flashcard.getId(),
                null);

        if (numOfRowsUpdated == 0) {
            throw new DatabaseUpdateException();
        }
        return numOfRowsUpdated;
    }

    /**
     * Reset and delete everything in the database. WARNING: use with care, for testing only pls. ლ(ಠ_ಠ ლ)
     */
    public void resetDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.resetDatabase(db);
    }
}
