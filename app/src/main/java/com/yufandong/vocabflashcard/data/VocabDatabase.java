package com.yufandong.vocabflashcard.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by YuFan on 3/30/16.
 */
public class VocabDatabase extends SQLiteOpenHelper {

    private static VocabDatabase instance;
    public static final String DATABASE_NAME = "Vocab.db";
    public static int DATABASE_VERSTION = 1;

    public VocabDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSTION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ENTRIES_SET);
        db.execSQL(CREATE_ENTRIES_WORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetDatabase(db);
    }

    public void resetDatabase(SQLiteDatabase db) {
        db.execSQL(DELETE_ENTRIES_SET);
        db.execSQL(DELETE_ENTRIES_WORD);
        onCreate(db);
    }

    public static abstract class Set implements BaseColumns {
        public static final String TABLE_NAME = "VocabSet";
        public static final String NAME = "name";
    }

    public static abstract class Word implements BaseColumns {
        public static final String TABLE_NAME = "VocabWord";
        public static final String SET_ID = "setId";
        public static final String FRONT = "front";
        public static final String BACK = "back";
    }

    private static final String COMMA = ", ";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";

    private static final String CREATE_ENTRIES_SET =
            "CREATE TABLE " + Set.TABLE_NAME + "(" +
                    Set._ID + " INTEGER PRIMARY KEY, " +
                    Set.NAME + TEXT_TYPE + " );";

    private static final String CREATE_ENTRIES_WORD =
            "CREATE TABLE " + Word.TABLE_NAME + " (" +
                    Word._ID + " INTEGER PRIMARY KEY, " +
                    Word.SET_ID + INTEGER_TYPE + COMMA +
                    Word.FRONT + TEXT_TYPE + COMMA +
                    Word.BACK + TEXT_TYPE + " )";

    private static final String DELETE_ENTRIES_SET =
            "DROP TABLE IF EXISTS " + Set.TABLE_NAME;

    private static final String DELETE_ENTRIES_WORD =
            "DROP TABLE IF EXISTS " + Word.TABLE_NAME;
}
