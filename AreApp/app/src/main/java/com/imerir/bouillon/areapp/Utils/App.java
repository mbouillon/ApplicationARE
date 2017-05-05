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

    public void onCreate() {
        super.onCreate();

        //Create instances
        WebServiceUserClient.createInstance(this);
        WebServiceOfferClient.createInstance(this);
        WebServiceMessageClient.createInstance(this);
        WebServiceDocumentClient.createInstance(this);
    }




    public static String md5(final String s) {
        final String MD5 = "MD5";
        String response = "";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            response = hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return response;
    }


    public static void sendMailIsValid(String userMail, Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="http://10.0.2.2:5000/sendCheckMail/" +  userMail  ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

}
