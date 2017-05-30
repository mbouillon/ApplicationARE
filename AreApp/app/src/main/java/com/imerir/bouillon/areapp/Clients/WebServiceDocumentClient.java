package com.imerir.bouillon.areapp.Clients;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.imerir.bouillon.areapp.Models.Document;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Sire Rémy
 * @version 0.9
 * Classe gérant les appels serveurs pour la table documents de la BDD
 */
public class WebServiceDocumentClient {

    private static WebServiceDocumentClient instance;

    private Context context;
    private RequestQueue queue;

    private ArrayList<Document> documents;
    private HashMap<Integer, Document> documentHash;

    /**
     * Methode permettant de créer l'instance de la classe
     * @param appContext
     */
    public static void createInstance(Context appContext){
        instance = new WebServiceDocumentClient(appContext);
    }

    /**
     * Methode permettant de récuperer l'instance de la classe
     * @return
     */
    public static WebServiceDocumentClient getInstance() {
        return instance;
    }

    /**
     * Cosntructeur de la classe
     * recupere le contexte de l'app et construit une volley request à partir de celui ci
     * @param context
     */
    private WebServiceDocumentClient(Context context) {
        this.context = context;

        queue = Volley.newRequestQueue(context);
        queue.start();

        documentHash = new HashMap<>();
    }

    /**
     * Methode qui envoie la http request au serveur
     * Il recupère le resultat sous forme de json et le convertit en un tableau d'objets Documents
     * @param listener
     */
    public void requestDocuments(final OnDocumentsListListener listener) {

        //String apiUrl = "https://desolate-hollows-18116.herokuapp.com/mobile/DocumentList/";
        String apiUrl = "http://10.0.2.2:5000/mobile/DocumentList/";
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

    /**
     * Methode permettant de recuperer tous les documents
     * @return
     */
    public ArrayList<Document> getDocuments() {
        return documents;
    }

    /**
     * Methode permettant de recuperer un document par rapport a son id
     * @param id
     * @return
     */
    public Document getDocument(int id) {
        return documentHash.get(id);
    }

    /**
     * Methode envoyant une requete HTTP DELETE au serveur supprimant un document en fonction de son id
     * @param id
     */
    public void DELETEDocument(int id) {
        String apiUrl = "http://10.0.2.2:5000/mobile/Document/" + id;
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

    /**
     * Interface de gestion pour listenner
     */
    public interface OnDocumentsListListener {
        void onDocumentsReceived(ArrayList<Document> Documents);
        void onDocumentsFailed(String error);
    }

}
