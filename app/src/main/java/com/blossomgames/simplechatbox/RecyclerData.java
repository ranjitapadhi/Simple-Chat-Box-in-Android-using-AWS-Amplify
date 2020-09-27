package com.blossomgames.simplechatbox;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerData {
    String title = "SUNIL BEHERA";
    String description;

    UserMessage userMessage;

    RecyclerView data;

    public UserMessage Msg(){
        return userMessage;
    }

    public String getSender() {
        return title;
    }
    public void setSender(String title) {
        this.title = title;
    }
    public String getMessage() {
        return description;
    }
    public void setMessage(String description) {
        this.description = description;
    }
}