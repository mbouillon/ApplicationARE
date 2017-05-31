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
import com.imerir.bouillon.areapp.Models.User;
import com.imerir.bouillon.areapp.Models.WelcomeMessage;

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
 * Classe gérant les appels serveurs pour la table Messages de la BDD
 */
public class WebServiceMessageClient {

    private static WebServiceMessageClient instance;

    private Context context;
    private RequestQueue queue;

    private ArrayList<WelcomeMessage> messages;
    private HashMap<Integer, WelcomeMessage> messageHash;


    /**
     * Methode permettant de créer l'instance de la classe
     * @param appContext
     */
    public static void createInstance(Context appContext) {
        instance = new WebServiceMessageClient(appContext);
    }

    /**
     * Methode permettant de récuperer l'instance de la classe
     * @return
     */
    public static WebServiceMessageClient getInstance() {return instance; }

    /**
     * Cosntructeur de la classe
     * recupere le contexte de l'app et construit une volley request à partir de celui ci
     * @param context
     */
    private WebServiceMessageClient(Context context){
        this.context = context;

        queue = Volley.newRequestQueue(context);
        queue.start();
        messageHash = new HashMap<>();
    }

    /**
     * Methode qui envoie la http request au serveur
     * Il recupère le dernier message sous forme de json
     * @param listener
     */
    public void requestMessages(final OnMessagesListListener listener, String url){
        //String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/LastMessage/";
        String apiUrl = url + "last/";
        JsonObjectRequest request = new JsonObjectRequest(
                apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            messages = new ArrayList<>();
                            JSONArray messagesJSON = response.getJSONArray("Message");
                            for (int i = 0; i < messagesJSON.length(); i++) {
                                JSONObject messageJSON = messagesJSON.getJSONObject(i);
                                WelcomeMessage message = new WelcomeMessage(messageJSON);
                                messages.add(message);
                                messageHash.put(message.getId(), message);
                                Log.d("Message", "Id : " + message.getId());
                            }
                            listener.onMessagesReceived(messages);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onMessagesFailed("Erreur inconnue");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onMessagesFailed(error.toString());
                    }
                });
        queue.add(request);
    }

    /**
     * Methode qui envoie la http request au serveur
     * Il recupère le resultat sous forme de json et le convertit en un tableau d'objets Messages
     * @param listener
     */
    public void requestAllMessages(final OnMessagesListListener listener, String url){
        //String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/MessagesList/";
        String apiUrl = url;
        JsonObjectRequest request = new JsonObjectRequest(
                apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            messages = new ArrayList<>();
                            JSONArray messagesJSON = response.getJSONArray("Message");
                            for (int i = 0; i < messagesJSON.length(); i++) {
                                JSONObject messageJSON = messagesJSON.getJSONObject(i);
                                WelcomeMessage message = new WelcomeMessage(messageJSON);
                                messages.add(message);
                                messageHash.put(message.getId(), message);
                                Log.d("Message", "Id : " + message.getId());
                            }
                            listener.onMessagesReceived(messages);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onMessagesFailed("Erreur inconnue");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onMessagesFailed(error.toString());
                    }
                });
        queue.add(request);
    }

    /**
     * Methode construisant un json a partir d'un messageObject et envoie une requete post a la bdd
     * @param message
     */
    //Methode de post de message
    public void POSTMessage(WelcomeMessage message, String url) {
        final HashMap<String, String> params = new HashMap<String, String>();
        //String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/Message/";
        String apiUrl = url;
        Gson gson = new Gson();
        final String json = gson.toJson(message);
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
     * Methode qui retourne la liste de tous les messages
     * @return
     */
    public ArrayList<WelcomeMessage> getMessages() {return messages;}

    /**
     * Methode qui retourne un message en fonction de son id
     * @param id
     * @return
     */
    public WelcomeMessage getMessage(String id) {return messageHash.get(id);}

    /**
     * Methode envoyant une requete HTTP DELETE au serveur supprimant un message en fonction de son id
     * @param id
     */
    public void DELETEMessage(int id, String url) {
        String apiUrl = url + id;
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

    public interface OnMessagesListListener {
        void onMessagesReceived(ArrayList<WelcomeMessage> messages);
        void onMessagesFailed(String error);
    }
}
