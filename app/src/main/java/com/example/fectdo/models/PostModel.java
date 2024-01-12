package com.example.fectdo.models;

import com.google.firebase.Timestamp;

public class PostModel {
    String userId;
    String postId;
    String username;
    String timestamp;//I will use yyyyMMMMddHHmm format so it can be sort easily
    String postDescription;
    int like;
    int level;

    public PostModel() {
    }

    public PostModel(String userId, String postId, String username, String timestamp, String postDescription, int like) {
        this.userId = userId;
        this.postId = postId;
        this.username = username;
        this.timestamp = timestamp;
        this.postDescription = postDescription;
        this.like = like;
    }

    public String getUserId() {
        return userId;
    }

    public String getPostId() {
        return postId;
    }

    public String getUsername() {
        return username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getLike() {
        return like;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
