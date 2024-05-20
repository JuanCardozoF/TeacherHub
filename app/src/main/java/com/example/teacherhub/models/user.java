package com.example.teacherhub.models;

public class user {
    private String id;
    private String username;
    private String email;
    private String password;
    private boolean is_active;
    private String id_role;

    public user(String id, String username, String email, String password, boolean is_active, String id_role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.is_active = is_active;
        this.id_role = id_role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getId_role() {
        return id_role;
    }

    public void setId_role(String id_role) {
        this.id_role = id_role;
    }
}
