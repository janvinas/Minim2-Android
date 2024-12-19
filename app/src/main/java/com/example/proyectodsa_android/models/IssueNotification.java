package com.example.proyectodsa_android.models;

import com.google.gson.annotations.Expose;

public class IssueNotification {
    @Expose
    private String date;
    @Expose
    private String title;
    @Expose
    private String message;
    @Expose
    String sender;

    public IssueNotification(){}

    public IssueNotification(String date, String title, String message, String sender){
        this.date = date;
        this.title = title;
        this.message = message;
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "date: " + date + ", title: " + title + ", message: " + message;
    }
}
