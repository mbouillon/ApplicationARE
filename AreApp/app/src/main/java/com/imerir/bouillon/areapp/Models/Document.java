package com.imerir.bouillon.areapp.Models;

import org.json.JSONObject;

/**
 * @author Bouillon Maxime
 * @version 0.9
 * Modèle objet de Document avec ses getters et setters Il se construit directement a partir d'un JSONObject
 */

public class Document {

    private int DocId;
    private int PublisherId;
    private String PublishDate;
    private String DocumentURL;

    /**
     *
     * @param json
     */
    public Document(JSONObject json) {
        DocId = json.optInt("DocID");
        PublisherId = json.optInt("PublisherId");
        PublishDate = json.optString("PublishDate");
        DocumentURL = json.optString("Document");
    }

    /**
     *
     * @return
     */
    public int getDocId() {
        return DocId;
    }

    /**
     *
     * @param docId
     */
    public void setDocId(int docId) {
        this.DocId = docId;
    }

    /**
     *
     * @return
     */
    public int getPublisherId() { return PublisherId; }

    /**
     *
     * @param publisherId
     */
    public void setPublisherId(int publisherId) {
        PublisherId = publisherId;
    }

    /**
     *
     * @return
     */
    public String getPublishDate() {
        return PublishDate;
    }

    /**
     *
     * @param publishDate
     */
    public void setPublishDate(String publishDate) {
        PublishDate = publishDate;
    }

    /**
     *
     * @return
     */
    public String getDocumentURL() {
        return DocumentURL;
    }

    /**
     *
     * @param documentURL
     */
    public void setDocumentURL(String documentURL) {
        this.DocumentURL = documentURL;
    }

}
