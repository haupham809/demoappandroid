package com.example.demochatapp.model;

import java.io.Serializable;

public class user implements Serializable {
    public String name;
    public String username;
    public String email;
    public String pass;


    public user() {
    }

    public user(String name,String username, String email,String pass) {
        this.name = name;
        this.username = username;
        this.pass = pass;
        this.email = email;
    }
}