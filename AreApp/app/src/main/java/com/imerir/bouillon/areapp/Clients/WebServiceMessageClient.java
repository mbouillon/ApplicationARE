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
        //String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/LastMessage/";
        String apiUrl = "http://10.0.2.2:5000/mobile/LastMessage";
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
        //String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/MessagesList/";
        String apiUrl = "http://10.0.2.2:5000/mobile/MessagesList";
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

    //Methode de post de message
    public void POSTMessage(WelcomeMessage message) {
        final HashMap<String, String> params = new HashMap<String, String>();
        //String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/Message/";
        String apiUrl = "http://10.0.2.2:5000/mobile/Message/";
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





    public ArrayList<WelcomeMessage> getMessages() {return messages;}

    public WelcomeMessage getMessage(String id) {return messageHash.get(id);}

    public void DELETEMessage(int id) {
        String apiUrl = "http://10.0.2.2:5000/mobile/Message/" + id;
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
