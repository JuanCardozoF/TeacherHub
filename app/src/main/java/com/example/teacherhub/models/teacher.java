package com.example.teacherhub.models;

import java.util.ArrayList;

public class teacher {
    private String id;
    private String name;
    private ArrayList<course> curs;


    public teacher(String id, String name, ArrayList<course> curs) {
        this.id = id;
        this.name = name;
        this.curs = curs;
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

    public ArrayList<course> getCurses() {
        return curs;
    }

    public void setCurses(ArrayList<course> curs) {
        this.curs = curs;
    }
}
