package com.imerir.bouillon.areapp.Clients;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.imerir.bouillon.areapp.Models.Offer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maxime on 09/03/2017.
 */

/**
 * @author Bouillon Maxime
 * @version 0.9
 * Classe gérant les appels serveurs pour la table Offers de la BDD
 */

public class WebServiceOfferClient {

    private static WebServiceOfferClient instance;


    private Context context;
    private RequestQueue queue;

    private ArrayList<Offer> offers;
    private HashMap<Integer, Offer> offerHash;

    /**
     * Methode permettant de créer l'instance de la classe
     * @param appContext
     */
    public static void createInstance(Context appContext){
        instance = new WebServiceOfferClient(appContext);
    }

    /**
     * Methode permettant de récuperer l'instance de la classe
     * @return
     */
    public static WebServiceOfferClient getInstance(){
        return instance;
    }

    /**
     * Cosntructeur de la classe
     * recupere le contexte de l'app et construit une volley request à partir de celui ci
     * @param context
     */
    private WebServiceOfferClient(Context context){
        this.context = context;
        queue = Volley.newRequestQueue(context);
        offerHash = new HashMap<>();
        queue.start();

    }


    /**
     * Methode qui envoie la http request au serveur
     * Il recupère les offres sous forme de json et les convertitenun tableau d'objets Offres
     * @param listener
     */
    public void requestOffers(final OnOffersListListener listener) {
        //String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/OffersListLinkedWithContact/";
        String apiUrl = "http://10.0.2.2:5000/offers/linkedwithcontact/";
        JsonObjectRequest request = new JsonObjectRequest(
                apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            offers = new ArrayList<>();
                            JSONArray offersJSON = response.getJSONArray("Offre");
                            for (int i = 0; i < offersJSON.length(); i++) {
                                JSONObject offerJSON = offersJSON.getJSONObject(i);
                                Offer offer = new Offer(offerJSON);
                                offers.add(offer);
                                offerHash.put(offer.getOfferID(), offer);
                                Log.d("Offre", "ID : " + offer.getOfferID());
                            }
                            listener.onOffersReceived(offers);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onOffersFailed("Erreur inconnue");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onOffersFailed(error.toString());
                    }
                });
        queue.add(request);
    }

    /**
     * Retourne la liste des offres
     * @return
     */
    public ArrayList<Offer> getOffers(){
        return offers;
    }

    /**
     * Retourne une offre en fonction de son id
     * @param id
     * @return
     */
    public Offer getOffer(int id) {
        return offerHash.get(id);
    }


    /**
     * Poste une nouvelle offre en la serialisant en json et en l'envoyant en HTTP par POST
     * @param offer
     */
    public void POSTOffer(Offer offer) {
        final HashMap<String, String> params = new HashMap<String, String>();
        //String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/User/";
        String apiUrl = "http://10.0.2.2:5000/offers/linkedwithcontact/";
        Gson gson = new Gson();
        final String json = gson.toJson(offer);


        RequestQueue queue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Response
                    }}, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
            }
        }){
            @Override
            public byte[] getBody()throws AuthFailureError {
                return json.getBytes();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            public String getBodyContentType()
            {
                return "application/json";
            }
        };
        queue.add(stringRequest);
        queue.start();
    }

    /**
     * Methode permettant la modification d'une offre fonctionne comme un POST mais en envoyant un PUT au serveur modifiant les informations dans la BDD
     * @param offer
     * @param id
     */
    public void PUTOffer(Offer offer, int id) {
        final HashMap<String, String> params = new HashMap<String, String>();
        //String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/User/";
        String apiUrl = "http://10.0.2.2:5000/offers/" + id;
        Gson gson = new Gson();
        final String json = gson.toJson(offer);


        RequestQueue queue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(Request.Method.PUT, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Response
                    }}, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
            }
        }){
            @Override
            public byte[] getBody()throws AuthFailureError {
                return json.getBytes();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            public String getBodyContentType()
            {
                return "application/json";
            }
        };
        queue.add(stringRequest);
        queue.start();
    }


    /**
     * Methode qui permet de suprimmer une offre en fonction de son ID ENvoie un Delete en http au serveur
     * @param id
     */
    public void DELETEOffer(int id) {
        String apiUrl = "http://10.0.2.2:5000/offers/" + id;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Response
                    }}, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
            }
        }){
        };
        queue.add(stringRequest);
        queue.start();
    }

    public interface OnOffersListListener {
        void onOffersReceived(ArrayList<Offer> offers);
        void onOffersFailed(String error);
    }
}