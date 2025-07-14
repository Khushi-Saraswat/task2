package com.example.server.Response;

public class LoginRes {

    String jwt;
    String ROLE;

    public String getROLE() {
        return ROLE;
    }

    public void setROLE(String rOLE) {
        ROLE = rOLE;
    }

    public LoginRes(String jwt, String ROLE) {
        this.jwt = jwt;
        this.ROLE = ROLE;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public String toString() {
        return "LoginRes [jwt=" + jwt + ", ROLE=" + ROLE + "]";
    }

}
