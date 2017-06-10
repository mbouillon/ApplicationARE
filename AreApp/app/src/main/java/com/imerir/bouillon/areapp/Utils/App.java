package com.imerir.bouillon.areapp.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.imerir.bouillon.areapp.Clients.WebServiceDocumentClient;
import com.imerir.bouillon.areapp.Clients.WebServiceMessageClient;
import com.imerir.bouillon.areapp.Clients.WebServiceOfferClient;
import com.imerir.bouillon.areapp.Clients.WebServiceUserClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by maxime on 07/03/2017.
 */

public class App extends Application {

     /**
     * Créée les instances des classes de client HTTP
     */
    public void onCreate() {
        super.onCreate();

        //Create instances
        WebServiceUserClient.createInstance(this);
        WebServiceOfferClient.createInstance(this);
        WebServiceMessageClient.createInstance(this);
        WebServiceDocumentClient.createInstance(this);
    }


    /**
     * Formate une chaîne en ajoutant un séparateur tous les <code>length</code> caractères.
     *
     * @param string Chaîne à formater
     * @param separator Séparateur à ajouter
     * @param length Taille des groupes de caractères à séparer
     * @return La chaîne formatée
     */
    public static String addChar(String string, String separator, int length) {

        if (string != null && string.length() > length) {

            string = string.replaceAll("([^\\s]{" + length + "})", "$0" + separator);
        }

        return string;
    }

}
