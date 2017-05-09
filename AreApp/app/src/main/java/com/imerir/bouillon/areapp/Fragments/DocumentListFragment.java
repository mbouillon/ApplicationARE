package com.imerir.bouillon.areapp.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.imerir.bouillon.areapp.Activities.MainActivity;
import com.imerir.bouillon.areapp.Activities.PdfActivity;
import com.imerir.bouillon.areapp.Adapters.DocumentAdapter;
import com.imerir.bouillon.areapp.Clients.WebServiceDocumentClient;
import com.imerir.bouillon.areapp.Models.Document;
import com.imerir.bouillon.areapp.R;
import com.imerir.bouillon.areapp.Utils.FilePath;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by maxime on 09/03/2017.
 */

public class DocumentListFragment extends Fragment implements View.OnClickListener, WebServiceDocumentClient.OnDocumentsListListener, DocumentAdapter.OnDocumentClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_document, null);
    }

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = DocumentListFragment.class.getSimpleName();
    private String selectedFilePath;

    //URL du serveur URL 10.0.2.2 localServeur Emulateur
    private String SERVER_URL = "http://10.0.2.2:5000/mobile/Document/";

    RecyclerView recyclerView;
    com.github.clans.fab.FloatingActionButton fbaAddDocument;

    //Progress Bar du chargement d'un document pdf
    ProgressDialog dialog;

    //Gestion de la Progress Bar des données
    ProgressDialog loadingDialog;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    int internet;

    ArrayList<Document> _document;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ///Si internet est activé alors on affiche un temps de chargement pour dl les données
        if (checkConnectivity() == 1){
            showLoadingDialog();
        }

        _document = new ArrayList<Document>();

        //Assisgnation du recycler view + Document Adapter + WebService
        WebServiceDocumentClient.getInstance().requestDocuments(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.documentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        WebServiceDocumentClient.getInstance().getDocuments();

        //FloactionActionButton
        fbaAddDocument = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.fbaAddDocument);
        fbaAddDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showFileChooser();
                    Toast.makeText(getActivity(),"Selectionnez le fichier a envoyer",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    //Permet de parcourir sont téléphone a la recherhce d'un document
    private void showFileChooser() {
        Intent intent = new Intent();
        //Définit le fichier de sélection sur tous les types de fichiers
        intent.setType("*/*");
        //Permet de sélectionner les données et de les retourner
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Démarre une nouvelle activité pour sélectionner les fichiers et retourner les données
        startActivityForResult(Intent.createChooser(intent,"Choisissez le fichier à envoyer.."),PICK_FILE_REQUEST);
    }

    @Override
    public void onDocumentClicked(Document document) {
        Intent intent = new Intent(getActivity(), PdfActivity.class);
        intent.putExtra("document_id", document.getDocId());
        startActivity(intent);
    }

    @Override
    public void onDocumentsReceived(ArrayList<Document> Documents) {
        Collections.reverse(Documents);
        recyclerView.setAdapter(new DocumentAdapter(Documents,this));
        //Récuperation des DOCUMENT, puis on renvoi la boîte de dialogue de progression
        loadingDialog.dismiss();
    }

    @Override
    public void onDocumentsFailed(String error) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //Aucune donnée présente
                    return;
                }
                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(getActivity(),selectedFileUri);
                Log.i(TAG,"Chemin du fichier sélectionné:" + selectedFilePath);

                //Permet l'envoie du fichier
                if(selectedFilePath != null){
                    dialog = ProgressDialog.show(getActivity(),"","Envoi du fichier...",true);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //Créer un nouveau thread à gérer Http Operations
                            uploadFile(selectedFilePath);
                        }
                    }).start();
                }
            }
        }
    }

    //Gestion du Progress Dialog
    public void showLoadingDialog(){
        try{
            //Création d'un ProgressDialog et l'afficher
            loadingDialog = ProgressDialog.show(getActivity(), "", "Chargement en cours...", true, false);
            //Création d'un Thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (progressStatus < 100) {
                        // Mise à jour l'état de progression
                        progressStatus += 1;
                        // Essayez de suspendre le Thread pendant x secondes
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // Mise à jour la barre de progression fictive
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // Mise à jour de l'état de progression
                                loadingDialog.setProgress(progressStatus);
                                // Si l'exécution de la tâche est terminée
                                if (progressStatus == 100) {
                                    //Renvoi la boîte de dialogue de progression
                                    loadingDialog.dismiss();
                                }
                            }
                        });
                    }
                }
            }).start(); // Démarrez l'opération
        }catch (Exception e){

        }
    }

    private int checkConnectivity() {
        boolean enabled = true;

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        //Vérification si le téléphone est connecté a un réseau mobile, wifi ou pas
        if ((info == null || !info.isConnected() || !info.isAvailable())) {
            //Aucune connexion internet
            internet = 0;
            Log.d("Internet", "OFF");
            Toast.makeText(getActivity(), "Connexion internet perdu.", Toast.LENGTH_SHORT).show();
            enabled = false;
        } else {
            //Le réseau est connecté
            Log.d("Internet", "ON");
            internet = 1;
        }
        return internet;
    }

    //TODO Déplacer vers WebServiceDocumentClient attention une methode doit fermer sa gueule #Micfer
    //TODO Aucun toast etc....
    //Android télécharger le fichier sur le serveur
    public int uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);

        if (!selectedFile.isFile()){
            dialog.dismiss();
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Autoriser les entrées
                connection.setDoOutput(true);//Autoriser les sorties
                connection.setUseCaches(false);//N'utilisez pas de copie en cache
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);

                //Création de nouveaux dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //Écriture d'octets sur le flux de sortie de données
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //Renvoie non. D'octets présents dans fileInputStream
                bytesAvailable = fileInputStream.available();
                //En sélectionnant la taille du tampon comme minimum d'octets disponibles ou 1 Mo
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //Définir le tampon en tant que tableau d'octets de taille de bufferSize
                buffer = new byte[bufferSize];

                //Lire les octets de FileInputStream(Du 0 indice de tampon à buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //Loop répète jusqu'à bytesRead = -1, c'est-à-dire, aucun octet est laissé à lire
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "La réponse du serveur est: " + serverResponseMessage + ": " + serverResponseCode);

                //Le code de réponse de 200 indique l'état du serveur OK
                if(serverResponseCode == 200){
                    Toast.makeText(getActivity(),"Chargement du fichier terminé.",Toast.LENGTH_SHORT).show();
                }

                //Fermeture les flux d'entrée et de sortie
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),"Fichier non trouvé",Toast.LENGTH_SHORT).show();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Erreur d'URL!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Impossible de lire / écrire un fichier!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Erreur inconnue", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }
    }
}
