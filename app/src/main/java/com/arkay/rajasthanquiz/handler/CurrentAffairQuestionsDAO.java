package com.arkay.rajasthanquiz.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arkay.rajasthanquiz.application.MainApplication;
import com.arkay.rajasthanquiz.beans.CurrentAffairLevel;
import com.arkay.rajasthanquiz.beans.CurrentAffairQuestion;

import java.util.ArrayList;
import java.util.Collections;


public class CurrentAffairQuestionsDAO {
    DatabaseHelper databaseHalper;

    public CurrentAffairQuestionsDAO(Context context){
        this.databaseHalper = MainApplication.getInstance(context);
    }

    public void addCurrentAffairLevel(CurrentAffairLevel currentAffairLevel){
        if(getTotalNoOfLevelByLevelID(currentAffairLevel.getCurrentAffairLevelID())==0) {
            SQLiteDatabase db = databaseHalper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.CURRENT_AFFAIR_LEVEL_ID, currentAffairLevel.getCurrentAffairLevelID());
            values.put(DatabaseHelper.CURRENT_AFFAIR_LEVEL_NAME, currentAffairLevel.getCurrentAfairStr());
            values.put(DatabaseHelper.CURRENT_AFFAIR_NO_USER_PLAY, 0);
            values.put(DatabaseHelper.CURRENT_AFFAIR_SCORE, 0);
            values.put(DatabaseHelper.CURRENT_AFFAIR_IS_LEVEL_PLAY,currentAffairLevel.is_ca_level_play());
            db.insert(DatabaseHelper.CURRENT_AFFAIR_LEVEL_TAB, null, values);
            //System.out.println("Size afte Add: " + getCurrentAffairLevels().size());
        }else{
            System.out.println("Alredy Exit");
        }
    }

    public void addCurrentAffairQuestion(int levelID, CurrentAffairQuestion currentAffairQuestion){
       // Log.i(HomeActivity.TAG, "Question: "+currentAffairQuestion);
        if(getTotalNoOfQuestionByQuestionID(currentAffairQuestion.getQuestionID())==0) {
            SQLiteDatabase db = databaseHalper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.CURRENT_QUESTION_ID, currentAffairQuestion.getQuestionID());
            values.put(DatabaseHelper.CURRENT_QUESTION_STR, currentAffairQuestion.getQuestion());
            values.put(DatabaseHelper.CURRENT_QUESTION_OPTION_A, currentAffairQuestion.getOptions().get(0));
            values.put(DatabaseHelper.CURRENT_QUESTION_OPTION_B, currentAffairQuestion.getOptions().get(1));
            values.put(DatabaseHelper.CURRENT_QUESTION_OPTION_C, currentAffairQuestion.getOptions().get(2));
            values.put(DatabaseHelper.CURRENT_QUESTION_OPTION_D, currentAffairQuestion.getOptions().get(3));
            values.put(DatabaseHelper.CURRENT_AFFAIR_LEVEL_ID, levelID);
            db.insert(DatabaseHelper.CURRENT_QUESTION_TAB, null, values);

        }else{
            System.out.println("Alredy Exit");
        }
        //System.out.println("Size afte Add: "+get().size());
    }
    public int getTotalNoOfQuestionByQuestionID(int questionID){

        String sql = "select count(*)  from "+DatabaseHelper.CURRENT_QUESTION_TAB +" where "+DatabaseHelper.CURRENT_QUESTION_ID +"= "+questionID;
        SQLiteDatabase db = databaseHalper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        int count =0;
        if (cursor.moveToFirst()){
            count = Integer.parseInt(cursor.getString(cursor.getColumnIndex("count(*)")));
        }
        cursor.close();
        db.close();
        return count;
    }

    public int getTotalNoOfLevelByLevelID(int levelID){

        String sql = "select count(*)  from "+DatabaseHelper.CURRENT_AFFAIR_LEVEL_TAB +" where "+DatabaseHelper.CURRENT_AFFAIR_LEVEL_ID +"= "+levelID;
        SQLiteDatabase db = databaseHalper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        int count =0;
        if (cursor.moveToFirst()){
            count = Integer.parseInt(cursor.getString(cursor.getColumnIndex("count(*)")));
        }
        cursor.close();
        db.close();
        return count;
    }

    public int getTotalNoOfLevel(){

        String sql = "select count(*)  from "+DatabaseHelper.CURRENT_AFFAIR_LEVEL_TAB;
        SQLiteDatabase db = databaseHalper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        int count =0;
        if (cursor.moveToFirst()){
            count = Integer.parseInt(cursor.getString(cursor.getColumnIndex("count(*)")));
        }
        cursor.close();
        db.close();
        return count;
    }

    public ArrayList<CurrentAffairLevel> getCurrentAffairLevels(){
        ArrayList<CurrentAffairLevel> tempCurrentAffairLevels = new ArrayList<CurrentAffairLevel>();

        String sql = "SELECT * FROM " + DatabaseHelper.CURRENT_AFFAIR_LEVEL_TAB +" order by "+DatabaseHelper.CURRENT_AFFAIR_LEVEL_ID+" desc";
       // Log.i("Gkin Gujarati",sql);
        SQLiteDatabase db = databaseHalper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()){
            do{
                boolean is_levle_paly =false;

                if(cursor.getColumnIndex(DatabaseHelper.CURRENT_AFFAIR_IS_LEVEL_PLAY)!=-1) {
                    is_levle_paly = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CURRENT_AFFAIR_IS_LEVEL_PLAY)) > 0;
                }

                CurrentAffairLevel currentAffairLevel = new CurrentAffairLevel(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CURRENT_AFFAIR_LEVEL_ID))), cursor.getString(cursor.getColumnIndex(DatabaseHelper.CURRENT_AFFAIR_LEVEL_NAME)), is_levle_paly, Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CURRENT_AFFAIR_SCORE))));
                tempCurrentAffairLevels.add(currentAffairLevel);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tempCurrentAffairLevels;
    }

    public ArrayList<CurrentAffairQuestion> getCurrentAffairQuestionsByLevelID(int currentAffairlevelID){
        ArrayList<CurrentAffairQuestion> currentAffairQuestions = new ArrayList<CurrentAffairQuestion>();
        //int total = 10;
        String sql = "select *  FROM "+ DatabaseHelper.CURRENT_QUESTION_TAB+" where "+DatabaseHelper.CURRENT_AFFAIR_LEVEL_ID +"= "+currentAffairlevelID;

        SQLiteDatabase db = databaseHalper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        int i=1;
        if (cursor.moveToFirst()){ // data?
            do{
                CurrentAffairQuestion question = new CurrentAffairQuestion(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CURRENT_QUESTION_ID)),cursor.getString(cursor.getColumnIndex(DatabaseHelper.CURRENT_QUESTION_STR)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.CURRENT_QUESTION_OPTION_A)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CURRENT_QUESTION_OPTION_A)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CURRENT_QUESTION_OPTION_B)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CURRENT_QUESTION_OPTION_C)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CURRENT_QUESTION_OPTION_D)));
                Collections.shuffle(question.getOptions());
                if(question.getOptions().size()==4){
                    currentAffairQuestions.add(question);
                }
                i++;
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Collections.shuffle(currentAffairQuestions);
        return currentAffairQuestions;

    }

    public boolean updateUserPlayQuizStatusAndScore(int currentAffairID, int levleScore) {
         SQLiteDatabase db = databaseHalper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.CURRENT_AFFAIR_IS_LEVEL_PLAY, true);
            values.put(DatabaseHelper.CURRENT_AFFAIR_SCORE, levleScore);
            db.update(DatabaseHelper.CURRENT_AFFAIR_LEVEL_TAB, values, DatabaseHelper.CURRENT_AFFAIR_LEVEL_ID + "= \"" + currentAffairID + "\"", null);
            db.close();
            return  true;

    }
    public void getTotalUserPlayQuiz(){
        String sql = "select count(*)  from "+DatabaseHelper.CURRENT_AFFAIR_LEVEL_TAB +" where "+DatabaseHelper.CURRENT_AFFAIR_IS_LEVEL_PLAY +"= 1";
        SQLiteDatabase db = databaseHalper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        int count =0;
        if (cursor.moveToFirst()){
            count = Integer.parseInt(cursor.getString(cursor.getColumnIndex("count(*)")));
        }
        cursor.close();
        db.close();
       // Log.i("Play: ",""+count);
       // return count;
    }


}

