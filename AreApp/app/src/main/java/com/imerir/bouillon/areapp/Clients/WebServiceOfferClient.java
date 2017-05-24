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


public class WebServiceOfferClient {

    private static WebServiceOfferClient instance;


    private Context context;
    private RequestQueue queue;

    private ArrayList<Offer> offers;
    private HashMap<Integer, Offer> offerHash;

    public static void createInstance(Context appContext){
        instance = new WebServiceOfferClient(appContext);
    }

    public static WebServiceOfferClient getInstance(){
        return instance;
    }

    private WebServiceOfferClient(Context context){
        this.context = context;
        queue = Volley.newRequestQueue(context);
        offerHash = new HashMap<>();
        queue.start();

    }

    public void requestOffers(final OnOffersListListener listener) {
        //String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/OffersListLinkedWithContact/";
        String apiUrl = "http://10.0.2.2:5000/mobile/OffersListLinkedWithContact/";
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

    public ArrayList<Offer> getOffers(){
        return offers;
    }

    public Offer getOffer(int id) {
        return offerHash.get(id);
    }



    public void POSTOffer(Offer offer) {
        final HashMap<String, String> params = new HashMap<String, String>();
        //String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/User/";
        String apiUrl = "http://10.0.2.2:5000/mobile/OfferLinkedWithContact/";
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

    public void PUTOffer(Offer offer) {
        final HashMap<String, String> params = new HashMap<String, String>();
        //String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/User/";
        String apiUrl = "http://10.0.2.2:5000/mobile/OfferLinkedWithContact/"+offer.getOfferID();
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



    public void DELETEOffer(int id) {
        String apiUrl = "http://10.0.2.2:5000/mobile/Offre/" + id;
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