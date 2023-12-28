package com.example.fectdo.models;

import com.google.firebase.firestore.Exclude;

public class TopicModel {
    private String topicName, videoLink;
    private String documentId;

    public TopicModel() {
    }

    public TopicModel(String topicName, String videoLink) {
        this.topicName = topicName;
        this.videoLink = videoLink;
    }
    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }
}
