package com.example.teacherhub.models;

public class token {
    private String user_role;
    private String user_id;
    private String sub;
    private String TokenString;
    private static token Instance;
    public token(String user_role, String user_id, String sub, String TokenString) {
        this.user_role = user_role;
        this.user_id = user_id;
        this.sub = sub;
        this.TokenString = TokenString;
        Instance = token.this;

    }

    public token() {

    }

    public String getUser_role() {return user_role; }
    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getSub() {
        return sub;
    }
    public void setSub(String sub) {
        this.sub = sub;
    }
    public String getTokenSring() { return TokenString; }
    public void setTokenSring(String tokenSring) { TokenString = tokenSring;}
    public static token getInstanceToke() {return  Instance; }

}
