package com.imerir.bouillon.areapp.Models;

import org.json.JSONObject;

/**
 * Created by maxime on 09/03/2017.
 */

public class WelcomeMessage {
    private int id;
    private int publisherId;
    private String message;

    public WelcomeMessage(JSONObject json) {
        id = json.optInt("MessageID");
        publisherId = json.optInt("PublisherId");
        message = json.optString("Message");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
