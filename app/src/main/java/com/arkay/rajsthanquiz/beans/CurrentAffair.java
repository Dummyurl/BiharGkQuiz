package com.arkay.rajsthanquiz.beans;

/**
 * Created by India on 13-04-2017.
 */

public class CurrentAffair {
    public String current_id;
    public String date;
    public String score;
    public String text;
    public boolean is_ca_level_play = false;

    public CurrentAffair(String current_id, String date, String score, String text) {
        this.current_id = current_id;
        this.date = date;
        this.score = score;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCurrent_id() {
        return current_id;
    }

    public void setCurrent_id(String current_id) {
        this.current_id = current_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
