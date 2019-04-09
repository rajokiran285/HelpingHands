package com.example.helpinghands.model;

public class User {

    String id,username,imageurl,status,search;


    public User(String userid, String username, String imageurl, String status,String search) {
        this.id = id;
        this.username = username;
        this.imageurl = imageurl;
        this.status=  status;
        this.search= search;
    }

    public User() {


    }

//    public String getUserid() {
//        return userid;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public void setUserid(String userid) {
//        this.userid = userid;
//    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
