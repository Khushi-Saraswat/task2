package com.example.server.Response;

public class LoginRes {
    
    String jwt;
    String message;
   
   
    public LoginRes(String jwt, String message) {
        this.jwt = jwt;
        this.message = message;
    }


    public String getJwt() {
        return jwt;
    }


    public void setJwt(String jwt) {
        this.jwt = jwt;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "LoginRes [jwt=" + jwt + ", message=" + message + "]";
    }

    
}
