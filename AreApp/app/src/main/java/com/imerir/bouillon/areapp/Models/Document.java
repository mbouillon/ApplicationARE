package com.imerir.bouillon.areapp.Models;

import org.json.JSONObject;

/**
 * Created by maxime on 09/03/2017.
 */

public class Document {

    private int DocId;
    private int PublisherId;
    private String PublishDate;
    private String DocumentURL;

    public Document(JSONObject json) {
        DocId = json.optInt("DocID");
        PublisherId = json.optInt("PublisherId");
        PublishDate = json.optString("PublishDate");
        DocumentURL = json.optString("Document");
    }

    public int getDocId() {
        return DocId;
    }

    public void setDocId(int docId) {
        this.DocId = docId;
    }

    public int getPublisherId() { return PublisherId; }

    public void setPublisherId(int publisherId) {
        PublisherId = publisherId;
    }

    public String getPublishDate() {
        return PublishDate;
    }

    public void setPublishDate(String publishDate) {
        PublishDate = publishDate;
    }

    public String getDocumentURL() {
        return DocumentURL;
    }

    public void setDocumentURL(String documentURL) {
        this.DocumentURL = documentURL;
    }

}
