package com.imerir.bouillon.areapp.Models;

import org.json.JSONObject;

/**
 * @author Bouillon Maxime
 * @version 0.9
 * Modèle objet de WelcomeMessage avec ses getters et setters Il se construit directement a partir d'un JSONObject
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
