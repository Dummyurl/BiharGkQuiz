package com.arkay.rajsthanquiz.beans;


import com.arkay.rajsthanquiz.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arkayapps on 15/02/16.
 */
public class LetLeranScore {
    private int lelveID = 0;
    private int levelScore;
    private boolean isLevelPlayed = false;

    public LetLeranScore(int lelveID) {
        this.lelveID = lelveID;
        this.levelScore =0;
        this.isLevelPlayed = false;
    }

    public LetLeranScore(int lelveID, int levelScore, boolean isLevelPlayed) {
        this.lelveID = lelveID;
        this.levelScore = levelScore;
        this.isLevelPlayed = isLevelPlayed;
    }

    public int getLelveID() {
        return lelveID;
    }

    public void setLelveID(int lelveID) {
        this.lelveID = lelveID;
    }

    public boolean isLevelPlayed() {
        return isLevelPlayed;
    }

    public void setIsLevelPlayed(boolean isLevelPlayed) {
        this.isLevelPlayed = isLevelPlayed;
    }

    public int getLevelScore() {
        return levelScore;
    }

    public void setLevelScore(int levelScore) {
        this.levelScore = levelScore;
    }

    public JSONObject getToStringJSON(){
        try {
            JSONObject scoreObject1 = new JSONObject();
            scoreObject1.put(MainActivity.LEVEL_ID,getLelveID());
            scoreObject1.put(MainActivity.LEVEL_SCORE,getLevelScore());
            scoreObject1.put(MainActivity.IS_LEVEL_PLAY,isLevelPlayed);
           // JSONObject obj = new JSONObject();
            //obj.put("scoredata", scoreObject1);
            return  scoreObject1;
        }catch (JSONException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error converting save data to JSON.", ex);
        }
    }
    @Override
    public String toString() {
        try {
            JSONObject scoreObject1 = new JSONObject();
            scoreObject1.put("levelID",getLelveID());
            scoreObject1.put("levelScore",getLevelScore());
            scoreObject1.put("isLevelPlay",isLevelPlayed);
            JSONObject obj = new JSONObject();
            obj.put("scoredata", scoreObject1);
            return  scoreObject1.toString();
        }catch (JSONException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error converting save data to JSON.", ex);
        }

    }
}
