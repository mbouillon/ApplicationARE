package com.imerir.bouillon.areapp.Models;

import org.json.JSONObject;

/**
 * @author Bouillon Maxime
 * @version 0.9
 * Modèle objet de Offer avec ses getters et setters Il se construit directement a partir d'un JSONObject
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

    /**
     *
     * @param json
     */
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


    /**
     *
     * @return
     */
    public int getOfferID() {
        return offerID;
    }

    /**
     *
     * @param offerID
     */
    public void setOfferID(int offerID) {
        this.offerID = offerID;
    }

    /**
     *
     * @return
     */
    public String getTitre() {
        return titre;
    }

    /**
     *
     * @param titre
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     *
     * @return
     */
    public String getDureeContrat() {
        return dureeContrat;
    }

    /**
     *
     * @param dureeContrat
     */
    public void setDureeContrat(String dureeContrat) {
        this.dureeContrat = dureeContrat;
    }

    /**
     *
     * @return
     */
    public int getTypeContrat() {
        return typeContrat;
    }

    /**
     *
     * @param typeContrat
     */
    public void setTypeContrat(int typeContrat) {
        this.typeContrat = typeContrat;
    }

    /**
     *
     * @return
     */
    public int getFormationAssociee() {
        return formationAssociee;
    }

    /**
     *
     * @param formationAssociee
     */
    public void setFormationAssociee(int formationAssociee) {
        this.formationAssociee = formationAssociee;
    }

    /**
     *
     * @return
     */
    public int getEtat() {
        return etat;
    }

    /**
     *
     * @param etat
     */
    public void setEtat(int etat) {
        this.etat = etat;
    }

    /**
     *
     * @return
     */
    public String getDetails() {
        return details;
    }

    /**
     *
     * @param details
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     *
     * @return
     */
    public String getDetailsResponsables() {
        return detailsResponsables;
    }

    /**
     *
     * @param detailsResponsables
     */
    public void setDetailsResponsables(String detailsResponsables) {
        this.detailsResponsables = detailsResponsables;
    }

    /**
     *
     * @return
     */
    public String getDatePublicaiton() {
        return datePublicaiton;
    }

    public void setDatePublicaiton(String datePublicaiton) {
        this.datePublicaiton = datePublicaiton;
    }

    /**
     *
     * @return
     */
    public int getContactID() {
        return contactID;
    }

    /**
     *
     * @param contactID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     *
     * @return
     */
    public String getNomContact() {
        return nomContact;
    }

    /**
     *
     * @param nomContact
     */
    public void setNomContact(String nomContact) {
        this.nomContact = nomContact;
    }

    /**
     *
     * @return
     */
    public String getNomEntreprise() {
        return nomEntreprise;
    }

    /**
     *
     * @param nomEntreprise
     */
    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    /**
     *
     * @return
     */
    public String getLieuEntreprise() {
        return lieuEntreprise;
    }

    /**
     *
     * @param lieuEntreprise
     */
    public void setLieuEntreprise(String lieuEntreprise) {
        this.lieuEntreprise = lieuEntreprise;
    }

    /**
     *
     * @return
     */
    public String getMailContact() {
        return mailContact;
    }

    /**
     *
     * @param mailContact
     */
    public void setMailContact(String mailContact) {
        this.mailContact = mailContact;
    }

    /**
     *
     * @return
     */
    public String getTelephoneContact() {
        return telephoneContact;
    }

    /**
     *
     * @param telephoneContact
     */
    public void setTelephoneContact(String telephoneContact) {
        this.telephoneContact = telephoneContact;
    }


}
