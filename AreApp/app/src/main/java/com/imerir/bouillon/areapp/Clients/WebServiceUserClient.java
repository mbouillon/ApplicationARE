package com.imerir.bouillon.areapp.Clients;

import android.content.Context;
import android.util.Log;
import android.view.View;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maxime on 07/03/2017.
 */

public class WebServiceUserClient {

    private static WebServiceUserClient instance;


    private Context context;
    private RequestQueue queue;

    private ArrayList<User> users;
    private HashMap<String, User> userHash;
    private HashMap<Integer, User> userHashById;

    public static void createInstance(Context appContext){
        instance = new WebServiceUserClient(appContext);
    }

    public static WebServiceUserClient getInstance(){
        return instance;
    }

    private WebServiceUserClient(Context context){
        this.context = context;

        queue = Volley.newRequestQueue(context);
        queue.start();
        userHash = new HashMap<>();
        userHashById = new HashMap<>();
    }

    public void requestUsers(final OnUsersListListener listener) {
        String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/UsersList/";
        JsonObjectRequest request = new JsonObjectRequest(
                apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            users = new ArrayList<>();
                            JSONArray usersJSON = response.getJSONArray("Utilisateur");
                            for (int i = 0; i < usersJSON.length(); i++) {
                                JSONObject userJSON = usersJSON.getJSONObject(i);
                                User user = new User(userJSON);
                                users.add(user);
                                userHash.put(user.getMail(), user);
                                userHashById.put(user.getId(),user);
                                Log.d("Utilisateur", "Mail : " + user.getMail());
                            }
                            listener.onUsersReceived(users);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onUsersFailed("Erreur inconnue");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onUsersFailed(error.toString());
                    }
                });
        queue.add(request);
    }

    public void POSTUser(User user) {
        final HashMap<String, String> params = new HashMap<String, String>();
        String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/User/";
        Gson gson = new Gson();
        final String json = gson.toJson(user);


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

    public ArrayList<User> getUsers(){
        return users;
    }

    public User getUser(String mail) {
        return userHash.get(mail);
    }

    public User getUserById(Integer id) { return  userHashById.get(id); }


    public interface OnUsersListListener {
        void onUsersReceived(ArrayList<User> users);
        void onUsersFailed(String error);
    }
}
