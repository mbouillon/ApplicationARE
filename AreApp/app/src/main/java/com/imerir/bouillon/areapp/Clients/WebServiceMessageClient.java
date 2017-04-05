package com.imerir.bouillon.areapp.Clients;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.imerir.bouillon.areapp.Models.WelcomeMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by maxime on 09/03/2017.
 */
public class WebServiceMessageClient {

    private static WebServiceMessageClient instance;

    private Context context;
    private RequestQueue queue;

    private ArrayList<WelcomeMessage> messages;
    private HashMap<Integer, WelcomeMessage> messageHash;

    public static void createInstance(Context appContext) {
        instance = new WebServiceMessageClient(appContext);
    }

    public static WebServiceMessageClient getInstance() {return instance; }

    private WebServiceMessageClient(Context context){
        this.context = context;

        queue = Volley.newRequestQueue(context);
        queue.start();
        messageHash = new HashMap<>();
    }

    public void requestMessages(final OnMessagesListListener listener){
        String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/LastMessage/";
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

    public void requestAllMessages(final OnMessagesListListener listener){
        String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/MessagesList/";
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

    public ArrayList<WelcomeMessage> getMessages() {return messages;}

    public WelcomeMessage getMessage(String id) {return messageHash.get(id);}

    public interface OnMessagesListListener {
        void onMessagesReceived(ArrayList<WelcomeMessage> messages);
        void onMessagesFailed(String error);
    }
}
