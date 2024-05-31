package com.example.teacherhub.models;

import com.google.gson.annotations.SerializedName;

public class TeacherCourse {
    private String id;

    @SerializedName("idTeacher")
    private String idTeacher;
    @SerializedName("idSubject")
    private String idSubject;

    public TeacherCourse(String id, String idTeacher, String idSubject) {
        this.id = id;
        this.idTeacher = idTeacher;
        this.idSubject = idSubject;
    }
}
