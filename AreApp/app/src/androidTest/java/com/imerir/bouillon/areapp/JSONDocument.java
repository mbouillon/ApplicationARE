package com.imerir.bouillon.areapp;

import android.support.test.InstrumentationRegistry;

import com.imerir.bouillon.areapp.Models.Document;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertTrue;

/**
 * Created by SireRemy on 30/05/2017.
 */

public class JSONDocument {

    //Test de la lesture du fichier document.json
    @Test
    public void testLectureDocument() throws IOException {
        assertTrue(loadJSONdocument("document.json")!=null);
    }

    //Test de construction d'un document à partir du fichier documentvide.json, le document est censé avoir un id avec une valeur de 0 car le champ id n'est pas renseigné
    @Test
    public void documentJSON_vide() throws Exception{
        //création du JSONObject avec le retour de la fonction loadJSONdocument() pour le fichier documentvide.json
        JSONObject json = new JSONObject(loadJSONdocument("documentvide.json"));
        //recupération de l'array document
        JSONArray documentArray = json.getJSONArray("Document");
        //récupération du premier objet dans l'array body
        JSONObject documentJSON = documentArray.getJSONObject(0);

        Assert.assertTrue(new Document(documentJSON).getDocId() == 0);
    }

    //Test de construction d'un document à partir du fichier document.json, le document est censé avoir un id avec une valeur de 1
    @Test
    public void documentJSON_plein() throws Exception{
        //création du JSONObject avec le retour de la fonction loadJSONdocument() pour le fichier document.json
        JSONObject json = new JSONObject(loadJSONdocument("document.json"));
        //recupération de l'array document
        JSONArray documentArray = json.getJSONArray("Document");
        //récupération du premier objet dans l'array body
        JSONObject documentJSON = documentArray.getJSONObject(0);

        Assert.assertTrue(new Document(documentJSON).getDocId() == 1);
    }

    //Fonction de chargement du fichier json
    public String loadJSONdocument(String file) {
        String json = null;
        try {

            InputStream is = InstrumentationRegistry.getTargetContext().getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
