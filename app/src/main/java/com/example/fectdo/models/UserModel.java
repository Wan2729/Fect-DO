package com.example.fectdo.models;

import com.google.firebase.Timestamp;

import java.util.List;

public class UserModel {
    private String username;
    private String emailAddress;
    private String description;


    private String photo;
    private String online;

    public UserModel(String emailAddress, String username, String online, String photo, String description) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.description = description;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }
}