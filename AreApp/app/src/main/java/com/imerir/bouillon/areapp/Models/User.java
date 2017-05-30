package com.imerir.bouillon.areapp.Models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author Bouillon Maxime
 * @version 0.9
 * Modèle objet de User avec ses getters et setters Il se construit directement a partir d'un JSONObject
 */

public class User implements Serializable {
    private int id;
    private boolean type;
    private String nom;
    private String prenom;
    private String mail;
    private String telephone;
    private int formation;
    private int cvID;
    private int lmID;
    private String etiquette;
    private String dashBoard;
    private int participations;

    /**
     *
     * @param json
     */
    public User(JSONObject json) {
        id              = json.optInt("id");
        type            = json.optBoolean("type");
        nom             = json.optString("Nom");
        prenom          = json.optString("Prenom");
        mail            = json.optString("Mail");
        telephone       = json.optString("Telephone");
        formation       = json.optInt("Formation");
        cvID            = json.optInt("cvID");
        lmID            = json.optInt("lmID");
        etiquette       = json.optString("Etiquette");
        dashBoard       = json.optString("DashBoard");
        participations  = json.optInt("Participations");
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public boolean getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(boolean type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public String getNom() {
        return nom;
    }

    /**
     *
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     *
     * @return
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     *
     * @param prenom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     *
     * @return
     */
    public String getMail() {
        return mail;
    }

    /**
     *
     * @param mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     *
     * @return
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     *
     * @param telephone
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     *
     * @return
     */
    public int getFormation() {
        return formation;
    }

    /**
     *
     * @param formation
     */
    public void setFormation(int formation) {
        this.formation = formation;
    }

    /**
     *
     * @return
     */
    public int getCvID() {
        return cvID;
    }

    /**
     *
     * @param cvID
     */
    public void setCvID(int cvID) {
        this.cvID = cvID;
    }

    /**
     *
     * @return
     */
    public int getLmID() {
        return lmID;
    }

    /**
     *
     * @param lmID
     */
    public void setLmID(int lmID) {
        this.lmID = lmID;
    }

    /**
     *
     * @return
     */
    public String getEtiquette() {
        return etiquette;
    }

    /**
     *
     * @param etiquette
     */
    public void setEtiquette(String etiquette) {
        this.etiquette = etiquette;
    }

    /**
     *
     * @return
     */
    public String getDashBoard() {
        return dashBoard;
    }

    /**
     *
     * @param dashBoard
     */
    public void setDashBoard(String dashBoard) {
        this.dashBoard = dashBoard;
    }

    /**
     *
     * @return
     */
    public int getParticipations() {
        return participations;
    }

    /**
     *
     * @param participations
     */
    public void setParticipations(int participations) {
        this.participations = participations;
    }
}
