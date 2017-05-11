package com.arkay.rajasthanquiz.handler;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{


    // Database Information
    public static final String DATABASE_NAME    = "database1.db";
    public static final int    DATABASE_VERSION = 6;

    public static final String QUESTION_TAB =           "questions";
    public static final String QUESTION_CATEGORY_ID =   "que_category_id";
    public static final String QUESTION_ID =            "question_id";
    public static final String QUESTION_STR =           "question";
    public static final String QUESTION_OPTION_A =      "option_a";
    public static final String QUESTION_OPTION_B =      "option_b";
    public static final String QUESTION_OPTION_C =      "option_c";
    public static final String QUESTION_OPTION_D=       "option_d";
  //  public static final String QUESTION_USER_LEVEL =       "user_level";
    public static final String QUESTION_BOOKMARK_STATUS= "is_bookmark";
    public static final String QUESTION_TYPE= "question_type_name";

    public static final String CURRENT_QUESTION_TAB =           "ca_questions";
    public static final String CURRENT_QUESTION_ID =            "ca_question_id";
    public static final String CURRENT_QUESTION_STR =           "ca_current_question";
    public static final String CURRENT_QUESTION_OPTION_A =      "ca_option_a";
    public static final String CURRENT_QUESTION_OPTION_B =      "ca_option_b";
    public static final String CURRENT_QUESTION_OPTION_C =      "ca_option_c";
    public static final String CURRENT_QUESTION_OPTION_D=       "ca_option_d";


    // manage Category Table Information
    public static final String CURRENT_AFFAIR_LEVEL_TAB    = "ca_level_info";
    public static final String CURRENT_AFFAIR_LEVEL_ID     = "ca_level_id";
    public static final String CURRENT_AFFAIR_LEVEL_NAME = "ca_level_name";
    public static final String CURRENT_AFFAIR_NO_USER_PLAY = "ca_no_user_play";
    public static final String CURRENT_AFFAIR_SCORE = "ca_avg_score";
    public static final String CURRENT_AFFAIR_IS_LEVEL_PLAY=       "ca_is_level_play";

    // manage Category Table Information
    public static final String GK_INFO_TAB    = "gk_info";
    public static final String GK_INFO_ID    = "newsid";
    public static final String GK_INFO_TITLE    = "title";
    public static final String GK_INFO_DESC    = "desc";
    public static final String GK_INFO_IMG_URL = "imagepath";
    public static final String GK_INFO_POST_DATE    = "date";
    public static final String GK_INFO_POST_BY    = "uname";
    public static final String GK_INFO_IMG_CREDIT = "imgcredit";
    public static final String LATITUDE = "latitude";
    public static final String LOGITUDE = "longitude";

    public static final String LET_PLAY_SCORE_TAB    = "let_play_score_tab";
    public static final String LET_PLAY_SCORE_ID    = "let_play_score_id";
    public static final String LET_PLAY_SCORE_SCORE    = "let_play_score";
    public static final String LET_PLAY_SCORE_IS_PLAYES    = "let_play_is_palyed";

    // manage Category Table Information
    public static final String CATEGORY_TAB    = "category";
    public static final String CATEGORY_ID     = "category_id";
    public static final String CATEGORY_NAME = "category_name";


    public DatabaseHelper(Context contex){
        super(contex, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createQuestionable = "CREATE TABLE IF NOT EXISTS " + QUESTION_TAB
                + "( "
                + QUESTION_CATEGORY_ID + " TEXT,"
                + QUESTION_ID + " INTEGER PRIMARY KEY,"
                + QUESTION_STR + " TEXT,"
                + QUESTION_OPTION_A + " TEXT,"
                + QUESTION_OPTION_B + " TEXT,"
                + QUESTION_OPTION_C + " TEXT,"
                + QUESTION_OPTION_D + " TEXT,"
                + QUESTION_BOOKMARK_STATUS + " TEXT,"
                + QUESTION_TYPE + " INTEGER)";

        String createCurrentQuestionable = "CREATE TABLE IF NOT EXISTS " + CURRENT_QUESTION_TAB
                + "( "
                + CURRENT_QUESTION_ID + " INTEGER PRIMARY KEY ,"
                + CURRENT_QUESTION_STR + " TEXT,"
                + CURRENT_QUESTION_OPTION_A + " TEXT,"
                + CURRENT_QUESTION_OPTION_B + " TEXT,"
                + CURRENT_QUESTION_OPTION_C + " TEXT,"
                + CURRENT_QUESTION_OPTION_D + " TEXT,"
                + CURRENT_AFFAIR_LEVEL_ID + " TEXT)";

        String createCategoryTable = "CREATE TABLE IF NOT EXISTS " + CATEGORY_TAB + "( " + CATEGORY_ID + " INTEGER primary key," + CATEGORY_NAME + " text)";
        String createCurrentAffairLeveTable = "CREATE TABLE IF NOT EXISTS "
                + CURRENT_AFFAIR_LEVEL_TAB
                + "( "
                + CURRENT_AFFAIR_LEVEL_ID + " INTEGER primary key,"
                + CURRENT_AFFAIR_LEVEL_NAME + " text, "
                + CURRENT_AFFAIR_NO_USER_PLAY +" INTEGER, "
                + CURRENT_AFFAIR_SCORE +" INTEGER, "
                + CURRENT_AFFAIR_IS_LEVEL_PLAY+" BOOLEAN )";

        String creategkinfo = "CREATE TABLE IF NOT EXISTS " + GK_INFO_TAB
                + "( "
                + GK_INFO_ID + " INTEGER PRIMARY KEY ,"
                + GK_INFO_TITLE + " TEXT,"
                + GK_INFO_DESC + " TEXT,"
                + GK_INFO_IMG_URL + " TEXT,"
                + GK_INFO_POST_DATE + " TEXT,"
                + GK_INFO_POST_BY + " TEXT,"
                + GK_INFO_IMG_CREDIT + " TEXT)";

        String letplayScore = "CREATE TABLE IF NOT EXISTS " + LET_PLAY_SCORE_TAB
                + "( "
                + LET_PLAY_SCORE_TAB + " INTEGER ,"
                + LET_PLAY_SCORE_SCORE + " INTEGER,"
                + LET_PLAY_SCORE_IS_PLAYES + " BOOLEAN)";


        db.execSQL(createQuestionable);
        db.execSQL(createCurrentQuestionable);
        db.execSQL(createCategoryTable);
        db.execSQL(createCurrentAffairLeveTable);
        db.execSQL(creategkinfo);
        db.execSQL(letplayScore);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO Auto-generated method stub
       // Log.i("Database Upgrade Call: ","Old Version: "+oldVersion +" : New Version: "+newVersion);


        String sql = "ALTER TABLE " + CURRENT_AFFAIR_LEVEL_TAB + " ADD COLUMN " +
                CURRENT_AFFAIR_IS_LEVEL_PLAY + " BOOLEAN DEFAULT 0";
        Log.i("Database Upgrade Call: ", "" + sql);

        String letplayScore = "CREATE TABLE IF NOT EXISTS " + LET_PLAY_SCORE_TAB
                + "( "
                + LET_PLAY_SCORE_TAB + " INTEGER ,"
                + LET_PLAY_SCORE_SCORE + " INTEGER,"
                + LET_PLAY_SCORE_IS_PLAYES + " BOOLEAN)";
                //db.execSQL(sql);
        if (newVersion > oldVersion) {
            try {
                db.execSQL(sql);
                db.execSQL(letplayScore);
            } catch (SQLException e) {
                Log.i("ADD COLUMN ", "ca_isLevel_play already exists");
            }
        }


        //onCreate(db);
    }



}
