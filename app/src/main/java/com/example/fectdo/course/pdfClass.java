package com.example.fectdo.course;

public class pdfClass {

    public String subject,topic, name,description,url;

    public pdfClass(){

    }

    public pdfClass(String subject, String topic, String name, String description,String url) {
        this.subject = subject;
        this.topic = topic;
        this.name = name;
        this.description = description;
        this.url = url;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
