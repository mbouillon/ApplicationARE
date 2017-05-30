package com.imerir.bouillon.areapp.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by SireRemy on 30/05/2017.
 */
public class UserTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void Json() {
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("Titre", "DEV ANDROID");
            jsonObject.put("Duree", "10 mois");
            jsonObject.put("FormationAssociee", "2");
            jsonObject.put("Contrat", "1");
            jsonObject.put("NomContact", "Roger Air");
            jsonObject.put("Lieu", "Perpignan");
            jsonObject.put("ContactMail", "air.roger@test.com");
            jsonObject.put("ContactPhone", "05050505005");
            jsonObject.put("NomEntreprise", "GOOGLE INC");
            jsonObject.put("Details", "DetailsDetails");
            jsonObject.put("DetailsResponsable", "DetailsDetailsDetailsDetails");
            //Création de l'objet json et POST dans la BDD + Après le POST desctruction de l'activité et rechargement de l'activité principale
            Offer offre = new Offer(jsonObject);

            if(offre.getTitre().equals("DEV ANDROID")){
                Log.d("getTitre", " ");
            }
            if ( offre.getDureeContrat().equals("10 mois")){
                Log.d("getDureeContrat", " ");
            }
            if (offre.getFormationAssociee() == 2){
                Log.d("getFormationAssociee", " ");
            }
            if ( offre.getContactID() == 1){
                Log.d("getContactID", " ");
            }
            if ( offre.getNomContact().equals("Roger Air")){
                Log.d("getNomContact", " ");
            }
            if ( offre.getMailContact().equals("air.roger@test.com")){
                Log.d("getMailContact", " ");
            }
            if ( offre.getTelephoneContact().equals("05050505005")){
                Log.d("getTelephoneContact", " ");
            }
            if ( offre.getNomEntreprise().equals("GOOGLE INC")){
                Log.d("getNomEntreprise", " ");
            }
            if ( offre.getDetails().equals("DetailsDetails")){
                Log.d("getDetails", " ");
            }
            if ( offre.getDetailsResponsables().equals("DetailsDetailsDetailsDetails")){
                Log.d("getDetailsResponsables", " ");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}