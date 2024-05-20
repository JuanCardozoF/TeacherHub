package com.example.teacherhub.models;

import java.util.ArrayList;

public class teacher {
    private String id;
    private String name;
    private ArrayList<curse> curses;


    public teacher(String id, String name, ArrayList<curse> curses) {
        this.id = id;
        this.name = name;
        this.curses = curses;
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

    public ArrayList<curse> getCurses() {
        return curses;
    }

    public void setCurses(ArrayList<curse> curses) {
        this.curses = curses;
    }
}
