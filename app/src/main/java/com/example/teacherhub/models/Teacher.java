package com.example.teacherhub.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Teacher {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("subjects")
    private ArrayList<Course> curs;


    public Teacher(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Course> getCurses() {
        return curs;
    }

    public void setCurses(ArrayList<Course> curs) {
        this.curs = curs;
    }
}
