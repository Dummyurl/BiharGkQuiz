package com.arkay.rajasthanquiz.handler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by arkayapps on 08/09/15.
 */
public class BookMarkDatabaseHelper extends SQLiteOpenHelper {


    // Database Information
    public static final String DATABASE_NAME = "gkingujaratibookmark.db";
    public static final int DATABASE_VERSION = 3;

    // manage BOOKMARK Table Information
    public static final String BOOKMARK_TAB = "bookmark_table";
    public static final String BOOKMARK_ID = "question_id";
    public static final String BOOKMARK_STATUS = "status";

    public BookMarkDatabaseHelper(Context contex) {
        super(contex, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createConfigurationSql = "create table " + BOOKMARK_TAB + "( "
                + BOOKMARK_ID + " text primary key," + BOOKMARK_STATUS + " text)";
        db.execSQL(createConfigurationSql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
