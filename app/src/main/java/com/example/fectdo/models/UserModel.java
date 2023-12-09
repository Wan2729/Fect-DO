package com.example.fectdo.models;

import com.google.firebase.Timestamp;

public class UserModel {
    private String phone;
    private String username;
    private Timestamp createdTimestamp;
    private String emailAddress;

    public UserModel() {
    }

    public UserModel(String phone, String username, Timestamp createdTimestamp) {
        this.phone = phone;
        this.username = username;
        this.createdTimestamp = createdTimestamp;
    }



    public UserModel(String phone, String username, Timestamp createdTimestamp, String emailAddress) {
        this.phone = phone;
        this.username = username;
        this.createdTimestamp = createdTimestamp;
        this.emailAddress = emailAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
