package com.imerir.bouillon.areapp.Clients;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.imerir.bouillon.areapp.Models.Document;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by maxime on 09/03/2017.
 */

public class WebServiceDocumentClient {

    private static WebServiceDocumentClient instance;

    private Context context;
    private RequestQueue queue;

    private ArrayList<Document> documents;
    private HashMap<Integer, Document> documentHash;

    public static void createInstance(Context appContext){
        instance = new WebServiceDocumentClient(appContext);
    }

    public static WebServiceDocumentClient getInstance() {
        return instance;
    }

    private WebServiceDocumentClient(Context context) {
        this.context = context;

        queue = Volley.newRequestQueue(context);
        queue.start();

        documentHash = new HashMap<>();
    }

    public void requestDocuments(final OnDocumentsListListener listener) {

        String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/DocumentList/";
        JsonObjectRequest request = new JsonObjectRequest(
                apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            documents = new ArrayList<>();
                            JSONArray documentsJSON = response.getJSONArray("Document");

                            for(int i = 0; i < documentsJSON.length(); i++) {
                                JSONObject documentJSON = documentsJSON.getJSONObject(i);
                                Document document = new Document(documentJSON);
                                documents.add(document);
                                documentHash.put(document.getDocId(), document);
                                Log.d("Document", "id : "+document.getDocId());

                            }
                            listener.onDocumentsReceived(documents);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onDocumentsFailed("Erreur inconnue");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onDocumentsFailed(error.toString());
                    }
                });
        queue.add(request);
    }

    public ArrayList<Document> getDocuments() {
        return documents;
    }

    public Document getDocument(int id) {
        return documentHash.get(id);
    }

    public interface OnDocumentsListListener {
        void onDocumentsReceived(ArrayList<Document> Documents);
        void onDocumentsFailed(String error);
    }

}
