package com.arkay.rajsthanquiz.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.arkay.rajsthanquiz.application.MainApplication;
import com.arkay.rajsthanquiz.beans.GKInfo;

import java.util.ArrayList;


public class GKinfoDAO {
    DatabaseHelper databaseHalper;

    public GKinfoDAO(Context context){
        this.databaseHalper = MainApplication.getInstance(context);
    }

    public void addGKInfo(GKInfo gkInfo){
        if(getTotalNoOfGKinfoByInfoID(gkInfo.getNewsid())==0) {
            SQLiteDatabase db = databaseHalper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.GK_INFO_ID, gkInfo.getNewsid());
            values.put(DatabaseHelper.GK_INFO_TITLE, gkInfo.getTitle());
            values.put(DatabaseHelper.GK_INFO_DESC, gkInfo.getDesc());
            values.put(DatabaseHelper.GK_INFO_IMG_URL, gkInfo.getImagepath());

            values.put(DatabaseHelper.GK_INFO_POST_DATE, gkInfo.getDate());
            values.put(DatabaseHelper.GK_INFO_POST_BY, gkInfo.getUname());
            values.put(DatabaseHelper.GK_INFO_IMG_CREDIT, gkInfo.getImgcredit());

            db.insert(DatabaseHelper.GK_INFO_TAB, null, values);
            System.out.println("Size afte Add: " + getGKInfo().size());
        } else{
            System.out.println("Alredy Exit");
        }
    }

    public int getData(){

        String s = "SELECT MIN("+DatabaseHelper.GK_INFO_ID +") FROM "+ DatabaseHelper.GK_INFO_TAB;
        String sql = "SELECT  * FROM " + DatabaseHelper.GK_INFO_TAB +" ORDER BY "+DatabaseHelper.GK_INFO_ID +" DESC LIMIT 1";
        SQLiteDatabase db = databaseHalper.getReadableDatabase();
        Cursor cursor = db.rawQuery(s, null);
        int count =0;
        if (cursor.moveToFirst()){
            count = Integer.parseInt(cursor.getString(cursor.getColumnIndex("MIN("+DatabaseHelper.GK_INFO_ID +")")));
        }
        cursor.close();
        db.close();
        return count;
    }


    public int getTotalNoOfGKinfoByInfoID(int infoID){

        String sql = "select count(*)  from "+DatabaseHelper.GK_INFO_TAB +" where "+DatabaseHelper.GK_INFO_ID +"= "+infoID;
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
    public GKInfo getGKNesByStoryID(int infoID){
        String sql = "select *  from "+DatabaseHelper.GK_INFO_TAB +" where "+DatabaseHelper.GK_INFO_ID +"= "+infoID;
        SQLiteDatabase db = databaseHalper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        int count =0;
        GKInfo gkinfo = new GKInfo();
        if (cursor.moveToFirst()){
           do{
              gkinfo = new GKInfo(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.GK_INFO_ID))), cursor.getString(cursor.getColumnIndex(DatabaseHelper.GK_INFO_TITLE)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.GK_INFO_DESC)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.GK_INFO_POST_DATE)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.GK_INFO_POST_BY)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.LATITUDE)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOGITUDE)),cursor.getString(cursor.getColumnIndex(DatabaseHelper.GK_INFO_IMG_CREDIT)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.GK_INFO_IMG_URL)));
              //CurrentAffairLevel currentAffairLevel = new CurrentAffairLevel(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.GK_INFO_ID))), cursor.getString(cursor.getColumnIndex(DatabaseHelper.CURRENT_AFFAIR_LEVEL_NAME)));
              //tempGkInfos.add(gkinfo);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return gkinfo;
    }

    public void updateGKInfo(GKInfo gkNewsInfo){
        SQLiteDatabase db = databaseHalper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.GK_INFO_ID, gkNewsInfo.getNewsid());
        values.put(DatabaseHelper.GK_INFO_TITLE, gkNewsInfo.getTitle());
        values.put(DatabaseHelper.GK_INFO_DESC, gkNewsInfo.getDesc());
        values.put(DatabaseHelper.GK_INFO_IMG_URL, gkNewsInfo.getImagepath());
        values.put(DatabaseHelper.GK_INFO_POST_DATE, gkNewsInfo.getDate());
        values.put(DatabaseHelper.GK_INFO_POST_BY, gkNewsInfo.getUname());
        values.put(DatabaseHelper.GK_INFO_IMG_CREDIT, gkNewsInfo.getImgcredit());

        db.update(DatabaseHelper.GK_INFO_TAB, values, DatabaseHelper.GK_INFO_ID + " = " + gkNewsInfo.getNewsid(), null);
    }

    public ArrayList<GKInfo> getGKInfo(){
        ArrayList<GKInfo> tempGkInfos = new ArrayList<GKInfo>();

        // System.out.println("Sponser Size: "+currentAffairLevels.size());
        String sql = "SELECT * FROM " + DatabaseHelper.GK_INFO_TAB +" order by "+DatabaseHelper.GK_INFO_ID +" desc";
        SQLiteDatabase db = databaseHalper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        GKInfo gkinfo = new GKInfo();
        if (cursor.moveToFirst()){
            do{
                gkinfo = new GKInfo(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.GK_INFO_ID))), cursor.getString(cursor.getColumnIndex(DatabaseHelper.GK_INFO_TITLE)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.GK_INFO_DESC)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.GK_INFO_POST_DATE)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.GK_INFO_POST_BY)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.LATITUDE)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOGITUDE)),cursor.getString(cursor.getColumnIndex(DatabaseHelper.GK_INFO_IMG_CREDIT)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.GK_INFO_IMG_URL)));
                tempGkInfos.add(gkinfo);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tempGkInfos;
    }
}

