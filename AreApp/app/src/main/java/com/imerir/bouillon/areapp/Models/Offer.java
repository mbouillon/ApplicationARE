package com.imerir.bouillon.areapp.Models;

import org.json.JSONObject;

/**
 * Created by maxime on 09/03/2017.
 */

public class Offer {

    private int offerID;
    private int typeContrat;
    private int formationAssociee;
    private int etat;
    private String details;
    private String detailsResponsables;
    private String datePublicaiton;
    private int contactID;
    private String nomContact;
    private String nomEntreprise;
    private String lieuEntreprise;
    private String mailContact;
    private String telephoneContact;
    private String titre;
    private String dureeContrat;

    public Offer(JSONObject json) {
        offerID = json.optInt("OfferID");
        titre = json.optString("Titre");
        dureeContrat = json.optString("Duree");
        typeContrat = json.optInt("Contrat");
        formationAssociee = json.optInt("FormationAssociee");
        etat = json.optInt("Etat");
        details = json.optString("Details");
        detailsResponsables = json.optString("DetailsResponsable");
        datePublicaiton = json.optString("DatePublication");
        contactID = json.optInt("ContactID");
        nomContact = json.optString("NomContact");
        nomEntreprise = json.optString("NomEntreprise");
        lieuEntreprise = json.optString("Lieu");
        mailContact = json.optString("ContactMail");
        telephoneContact = json.optString("ContactPhone");
    }



    public int getOfferID() {
        return offerID;
    }

    public void setOfferID(int offerID) {
        this.offerID = offerID;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDureeContrat() {
        return dureeContrat;
    }

    public void setDureeContrat(String dureeContrat) {
        this.dureeContrat = dureeContrat;
    }

    public int getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(int typeContrat) {
        this.typeContrat = typeContrat;
    }

    public int getFormationAssociee() {
        return formationAssociee;
    }

    public void setFormationAssociee(int formationAssociee) {
        this.formationAssociee = formationAssociee;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetailsResponsables() {
        return detailsResponsables;
    }

    public void setDetailsResponsables(String detailsResponsables) {
        this.detailsResponsables = detailsResponsables;
    }

    public String getDatePublicaiton() {
        return datePublicaiton;
    }

    public void setDatePublicaiton(String datePublicaiton) {
        this.datePublicaiton = datePublicaiton;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getNomContact() {
        return nomContact;
    }

    public void setNomContact(String nomContact) {
        this.nomContact = nomContact;
    }

    public String getNomEntreprise() {
        return nomEntreprise;
    }

    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    public String getLieuEntreprise() {
        return lieuEntreprise;
    }

    public void setLieuEntreprise(String lieuEntreprise) {
        this.lieuEntreprise = lieuEntreprise;
    }

    public String getMailContact() {
        return mailContact;
    }

    public void setMailContact(String mailContact) {
        this.mailContact = mailContact;
    }

    public String getTelephoneContact() {
        return telephoneContact;
    }

    public void setTelephoneContact(String telephoneContact) {
        this.telephoneContact = telephoneContact;
    }


}
