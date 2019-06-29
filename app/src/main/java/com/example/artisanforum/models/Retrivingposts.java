package com.example.artisanforum.models;

import com.example.artisanforum.models.RetrivingImageUrls;

import java.util.ArrayList;

public class Retrivingposts {
    private String descrip;
    private String timestamp;

    public Retrivingposts() {
    }

    public Retrivingposts(String descrip, String timestamp) {
        this.descrip = descrip;
        this.timestamp = timestamp;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setdescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void settimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
