package com.example.teacherhub.models;

public class grade {
    private  String id;
    private  student student;
    private  String idTeacherSubject;
    private String comment;
    private boolean isPositive;
    private double note;

    public grade(String id, student student, String idTeacherSubject, String comment, boolean isPositive, double note) {
        this.id = id;
        this.student = student;
        this.idTeacherSubject = idTeacherSubject;
        this.comment = comment;
        this.isPositive = isPositive;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public student getStudent() {
        return student;
    }

    public void setStudent(student student) {
        this.student = student;
    }

    public String getIdTeacherSubject() {
        return idTeacherSubject;
    }

    public void setIdTeacherSubject(String idTeacherSubject) {
        this.idTeacherSubject = idTeacherSubject;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isPositive() {
        return isPositive;
    }

    public void setPositive(boolean positive) {
        isPositive = positive;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }
}
