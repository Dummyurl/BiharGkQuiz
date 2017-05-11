package com.arkay.rajasthanquiz.beans;

/**
 * Created by ARKAY 05 on 27-09-2016.
 */
public class GkInfoDetail {
    private String newsid;
    private String title;
    private String imgcredit;
    private String imagepath;
    private String desc;
    private String uname;
    private String date;
    private String latitude;
    private String longitude;

    public GkInfoDetail() {
    }

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getImgcredit() {
        return imgcredit;
    }

    public void setImgcredit(String imgcredit) {
        this.imgcredit = imgcredit;
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

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
