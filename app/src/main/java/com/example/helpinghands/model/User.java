package com.example.helpinghands.model;

public class User {

    String userid,username,imageurl;


    public User(String userid, String username, String imageurl) {
        this.userid = userid;
        this.username = username;
        this.imageurl = imageurl;
    }

    public User() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }


    
}
