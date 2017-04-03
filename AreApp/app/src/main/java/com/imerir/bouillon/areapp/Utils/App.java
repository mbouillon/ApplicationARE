package com.imerir.bouillon.areapp.Utils;

import android.app.Application;

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
}
