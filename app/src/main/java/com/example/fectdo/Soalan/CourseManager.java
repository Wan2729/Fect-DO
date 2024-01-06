package com.example.fectdo.Soalan;

public class CourseManager {
    String topicName, videoLink;

    public CourseManager() {
    }

    public CourseManager(String topicName, String topicVideoList) {
        this.topicName = topicName;
        this.videoLink = topicVideoList;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getVideoLink() {
        return videoLink;
    }
}
