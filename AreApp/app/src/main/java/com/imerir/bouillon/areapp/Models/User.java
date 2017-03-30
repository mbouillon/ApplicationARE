package com.imerir.bouillon.areapp.Models;

import org.json.JSONObject;

/**
 * Created by maxime on 07/03/2017.
 */

public class User {
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
    private String password;
    private boolean isValid;

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
        password        = json.optString("Password");
        isValid         = json.optBoolean("IsValid");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getFormation() {
        return formation;
    }

    public void setFormation(int formation) {
        this.formation = formation;
    }

    public int getCvID() {
        return cvID;
    }

    public void setCvID(int cvID) {
        this.cvID = cvID;
    }

    public int getLmID() {
        return lmID;
    }

    public void setLmID(int lmID) {
        this.lmID = lmID;
    }

    public String getEtiquette() {
        return etiquette;
    }

    public void setEtiquette(String etiquette) {
        this.etiquette = etiquette;
    }

    public String getDashBoard() {
        return dashBoard;
    }

    public void setDashBoard(String dashBoard) {
        this.dashBoard = dashBoard;
    }

    public int getParticipations() {
        return participations;
    }

    public void setParticipations(int participations) {
        this.participations = participations;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }


}
