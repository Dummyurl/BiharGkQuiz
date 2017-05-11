package com.arkay.rajasthanquiz.beans;

/**
 * Created by India on 10-04-2017.
 */

public class Level {
    private String level_id;
    private String level_name;
    private String no_of_que;

    public Level(String level_id, String level_name, String no_of_que) {
        this.level_id = level_id;
        this.level_name = level_name;
        this.no_of_que = no_of_que;
    }

    public String getLevel_id() {
        return level_id;
    }

    public void setLevel_id(String level_id) {
        this.level_id = level_id;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public String getNo_of_que() {
        return no_of_que;
    }

    public void setNo_of_que(String no_of_que) {
        this.no_of_que = no_of_que;
    }
}
