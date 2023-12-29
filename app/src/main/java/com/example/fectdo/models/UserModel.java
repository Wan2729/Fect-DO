package com.example.fectdo.models;

import com.google.firebase.Timestamp;

import java.util.List;

public class UserModel {
    private static String username;
    private Timestamp createdTimestamp;
    private String emailAddress;
    private String description;
    private String fileUrl;

    public UserModel(String username, Timestamp createdTimestamp, String emailAddress,String description,String fileUrl) {
        this.username = username;
        this.createdTimestamp = createdTimestamp;
        this.emailAddress = emailAddress;
        this.description =description;
        this.fileUrl = fileUrl;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserModel.username = username;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
