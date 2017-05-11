package com.arkay.rajsthanquiz.beans;

/**
 * Created by India on 12-04-2017.
 */

public class GKInfo {
    public int newsid;
    public String title;
    public String desc ;
    public String date;
    public String uname;
    public String latitude;
    public String longitude;
    public String imgcredit;
    public String imagepath;

    public GKInfo() {
    }

    public GKInfo(int newsid, String title, String desc, String date, String uname, String latitude, String longitude, String imgcredit, String imagepath) {
        this.newsid = newsid;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.uname = uname;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgcredit = imgcredit;
        this.imagepath = imagepath;
    }

    public int getNewsid() {
        return newsid;
    }

    public void setNewsid(int newsid) {
        this.newsid = newsid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImgcredit() {
        return imgcredit;
    }

    public void setImgcredit(String imgcredit) {
        this.imgcredit = imgcredit;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
}
