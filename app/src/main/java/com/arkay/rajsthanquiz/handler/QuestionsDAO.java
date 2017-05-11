package com.arkay.rajsthanquiz.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.arkay.rajsthanquiz.application.MainApplication;
import com.arkay.rajsthanquiz.beans.Category;
import com.arkay.rajsthanquiz.beans.PlayQuizLevel;
import com.arkay.rajsthanquiz.beans.PlayQuizQuestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class QuestionsDAO {
    DatabaseHelper databaseHalper;
    private Context context;

    public QuestionsDAO(Context context){
        this.databaseHalper = MainApplication.getInstance(context);
        this.context = context;
    }
    public void getQuestionRendomWithCategoryID(PlayQuizLevel level, int categoryID){
        List<PlayQuizQuestion> levelQuestion = new ArrayList<PlayQuizQuestion>();
        levelQuestion = getCategoryQuestions(categoryID);
        Collections.shuffle(levelQuestion);
        level.setQuestion(levelQuestion.subList(0, level.getNoOfQuestion()));
    }
    public void getQuestionRendomWithUserLevel(PlayQuizLevel level){
        List<PlayQuizQuestion> levelQuestion = new ArrayList<PlayQuizQuestion>();
        levelQuestion = getFourOptionQuestionRendom(level.getLevelNo(), level.getNoOfQuestion());
        Collections.shuffle(levelQuestion);
        level.setQuestion(levelQuestion.subList(0, level.getNoOfQuestion()));
    }

    public ArrayList<PlayQuizQuestion> getSingleAnswareQuestion(PlayQuizLevel level){
        ArrayList<PlayQuizQuestion> quizQestionAnswer = new ArrayList<PlayQuizQuestion>();
        int quizLeve = level.getLevelNo();
        quizLeve--;
        int start = quizLeve * 20;
        //int end = start + 10;
        String sql = "SELECT * FROM "+DatabaseHelper.QUESTION_TAB +"  LIMIT 10 OFFSET "+start;
        Log.i("GK in Gujarati", ""+sql);
        SQLiteDatabase db = databaseHalper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        //int i=1;
        if (cursor.moveToFirst()){ // data?
            do{
                String isBookmarkstr = cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_BOOKMARK_STATUS));
                boolean isBookmark = false;
                if(isBookmarkstr.equalsIgnoreCase("1")){
                    isBookmark = true;
                }else{
                    isBookmark = false;
                }
                PlayQuizQuestion question = new PlayQuizQuestion(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_ID)),cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_STR)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_A)), isBookmark,cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_TYPE)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_A)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_B)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_C)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_D)));
                Collections.shuffle(question.getOptions());
                if(question.getOptions().size()==4){
                    quizQestionAnswer.add(question);
                }

               // i++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        //Log.i(HomeActivity.TAG, "Single Ans: Size: " + quizQestionAnswer.size());

        Collections.shuffle(quizQestionAnswer);
        level.setQuestion(quizQestionAnswer.subList(0, level.getNoOfQuestion()));

        return quizQestionAnswer;
    }

    public int getTotalSingleAnswareQuestionLevel(){

        String sql = "select count(*)  from "+DatabaseHelper.QUESTION_TAB+"";
        SQLiteDatabase db = databaseHalper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        int count =0;
        if (cursor.moveToFirst()){
            count = Integer.parseInt(cursor.getString(cursor.getColumnIndex("count(*)")));
        }
        cursor.close();
        db.close();
        int level =1;
        if(count>=1){
            level = count/20;
        }
        return level;
    }
    public ArrayList<Integer> getOLDBookmarkedQuestionIDs(){
        BookMarkDatabaseHelper bookMarkDatabaseHelper = new BookMarkDatabaseHelper(context);
        SQLiteDatabase db = bookMarkDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query(BookMarkDatabaseHelper.BOOKMARK_TAB, null, BookMarkDatabaseHelper.BOOKMARK_STATUS + "= 1", null, null, null, null);
        ArrayList<Integer> questionsIds = new ArrayList<Integer>();
        while(cursor.moveToNext()){
            questionsIds.add(cursor.getInt(cursor.getColumnIndex(BookMarkDatabaseHelper.BOOKMARK_ID)));
        }
        cursor.close();

        return questionsIds;
    }
    public boolean updateOLDBookmark(int quoteId, String status) {
        BookMarkDatabaseHelper bookMarkDatabaseHelper = new BookMarkDatabaseHelper(context);
        try
        {
            SQLiteDatabase db = bookMarkDatabaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.QUESTION_BOOKMARK_STATUS, status);
            db.update(DatabaseHelper.QUESTION_TAB, values, DatabaseHelper.QUESTION_ID + "= \"" + quoteId+"\"", null);
            db.close();
            return  true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  false;
        }
    }


    public boolean updateBookmark(int quoteId, boolean status) {

        try
        {
            SQLiteDatabase db = databaseHalper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.QUESTION_BOOKMARK_STATUS, status);
            db.update(DatabaseHelper.QUESTION_TAB, values, DatabaseHelper.QUESTION_ID + "= \"" + quoteId+"\"", null);
            db.close();
            return  true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  false;
        }
    }

    public List<PlayQuizQuestion> getFourOptionQuestionRendom(int levelNo, int noOfQuestion){
        List<PlayQuizQuestion> playQuizquestions = new ArrayList<PlayQuizQuestion>();
        int total = noOfQuestion +5;

        String sqlQuery;
        sqlQuery = "select *  FROM "+DatabaseHelper.QUESTION_TAB+" ORDER BY RANDOM() LIMIT "+total;

        SQLiteDatabase db = databaseHalper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);
        int i=1;
        if (cursor.moveToFirst()){ // data?
            do{
                boolean isBookmark = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_BOOKMARK_STATUS))>0;

                PlayQuizQuestion question = new PlayQuizQuestion(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_ID)),cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_STR)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_A)), isBookmark,  cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_TYPE)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_A)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_B)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_C)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_D)));
                Collections.shuffle(question.getOptions());
                if(question.getOptions().size()==4){
                    playQuizquestions.add(question);
                }
                i++;
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Collections.shuffle(playQuizquestions);
        playQuizquestions = playQuizquestions.subList(0, noOfQuestion);
        return playQuizquestions;
    }

    public ArrayList<PlayQuizQuestion> getBookmarkQuestions(){
        ArrayList<PlayQuizQuestion> playQuizquestions = new ArrayList<PlayQuizQuestion>();
        String sql = "select * from "+DatabaseHelper.QUESTION_TAB+" where "+ DatabaseHelper.QUESTION_BOOKMARK_STATUS+"  = 1";

        SQLiteDatabase db = databaseHalper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        System.out.println("cursor : "+cursor);
        int i=1;
        if (cursor.moveToFirst()){ // data?
            do{
                boolean isBookmark = true;
                PlayQuizQuestion question = new PlayQuizQuestion(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_ID)),cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_STR)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_A)), isBookmark, cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_TYPE)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_A)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_B)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_C)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_D)));

                if(question.getOptions().size()==4){
                    playQuizquestions.add(question);
                }
                i++;
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return playQuizquestions;
    }

    public ArrayList<PlayQuizQuestion> getCategoryQuestions(int categoryID){
        ArrayList<PlayQuizQuestion> playQuizquestions = new ArrayList<PlayQuizQuestion>();
        //String sql = "select * from "+DatabaseHelper.QUESTION_TAB+" where ('.' || que_category_id || '.') LIKE '%,"+categoryID+",%'";
        String sql = "select * from "+DatabaseHelper.QUESTION_TAB+" where ('.' || que_category_id || '.') LIKE '%."+categoryID+".%'";
      //  "select * from wuestions where ('.' || que_category_id || '.') LIKE '%.13.%'";

        //Log.i("Category: ",sql);

        SQLiteDatabase db = databaseHalper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        int i=1;
        if (cursor.moveToFirst()){ // data?
            do{
                boolean isBookmark = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_BOOKMARK_STATUS))>0;

                PlayQuizQuestion question = new PlayQuizQuestion(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_ID)),cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_STR)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_A)), isBookmark, cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_TYPE)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_A)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_B)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_C)));
                question.addOption(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_OPTION_D)));
                Collections.shuffle(question.getOptions());
                if(question.getOptions().size()==4){
                    playQuizquestions.add(question);
                }
                i++;
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return playQuizquestions;
    }
    public String getCategoryByCategoryID(int categoryID){

        String sql = "select * from "+DatabaseHelper.CATEGORY_TAB +" where "+DatabaseHelper.CATEGORY_ID +"= "+categoryID;
        SQLiteDatabase db = databaseHalper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        String categoryName ="";
        if (cursor.moveToFirst()){
            categoryName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CATEGORY_NAME));
        }
        cursor.close();
        db.close();
        return categoryName;
    }
}

