package com.yufandong.vocabflashcard.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Defines the schema of the SQLite database for storing vocab sets and words.
 */
public class SqliteDatabase extends SQLiteOpenHelper {

    private static SqliteDatabase instance;
    public static final String DATABASE_NAME = "Vocab.db";
    public static int DATABASE_VERSTION = 1;

    public SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSTION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_VOCAB_SET);
        db.execSQL(CREATE_TABLE_FLASHCARD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetDatabase(db);
    }

    public void resetDatabase(SQLiteDatabase db) {
        db.execSQL(DELETE_TABLE_VOCAB_SET);
        db.execSQL(DELETE_TABLE_FLASHCARD);
        onCreate(db);
    }

    public static abstract class VocabSetTable implements BaseColumns {
        public static final String TABLE_NAME = "VocabSet";
        public static final String NAME = "name";
    }

    public static abstract class FlashcardTable implements BaseColumns {
        public static final String TABLE_NAME = "VocabWord";
        public static final String SET_ID = "setId";
        public static final String FRONT = "front";
        public static final String BACK = "back";
    }

    private static final String COMMA = ", ";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";

    private static final String CREATE_TABLE_VOCAB_SET =
            "CREATE TABLE " + VocabSetTable.TABLE_NAME + "(" +
                    VocabSetTable._ID + " INTEGER PRIMARY KEY, " +
                    VocabSetTable.NAME + TEXT_TYPE + " );";

    private static final String CREATE_TABLE_FLASHCARD =
            "CREATE TABLE " + FlashcardTable.TABLE_NAME + " (" +
                    FlashcardTable._ID + " INTEGER PRIMARY KEY, " +
                    FlashcardTable.SET_ID + INTEGER_TYPE + COMMA +
                    FlashcardTable.FRONT + TEXT_TYPE + COMMA +
                    FlashcardTable.BACK + TEXT_TYPE + " )";

    private static final String DELETE_TABLE_VOCAB_SET =
            "DROP TABLE IF EXISTS " + VocabSetTable.TABLE_NAME;

    private static final String DELETE_TABLE_FLASHCARD =
            "DROP TABLE IF EXISTS " + FlashcardTable.TABLE_NAME;
}
