package com.imerir.bouillon.areapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.imerir.bouillon.areapp.Clients.WebServiceUserClient;
import com.imerir.bouillon.areapp.Models.User;
import com.imerir.bouillon.areapp.R;

import java.util.ArrayList;

/**
 * Created by SireRemy on 13/05/2017.
 */

public class ProfilStudentFragment extends Fragment implements View.OnClickListener, WebServiceUserClient.OnUsersListListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profil_student, null);
    }

    User user;

    private static final int PICK_FILE_REQUEST_CV = 1;
    private static final int PICK_FILE_REQUEST_LM = 1;
    private static final int PICK_FILE_REQUEST_ETIQUETTE = 1;
    private static final int PICK_FILE_REQUEST_DB = 1;
    private static final String TAG = ProfilStudentFragment.class.getSimpleName();
    private String selectedFilePath;

    //URL du serveur URL 10.0.2.2 localServeur Emulateur
    private String SERVER_URL_CV = "http://10.0.2.2:5000/mobile/cv/";
    private String SERVER_URL_LM = "http://10.0.2.2:5000/mobile/lm/";
    private String SERVER_URL_ETIQUETTE = "http://10.0.2.2:5000/mobile/etiquette/";
    private String SERVER_URL_DB = "http://10.0.2.2:5000/mobile/db/";

    ProgressDialog dialog;

    TextView firstname, secondname, mail, phoneNumber;

    TextView tvCV_load, tvLM_load, tvEtiquette_load, tvDB_load;
    ImageButton ibCV, ibLM, ibEtiquette, ibDB;

    com.github.clans.fab.FloatingActionButton floatingActionButtonEdited;

    //Gestion de la Progress Bar des données
    ProgressDialog loadingDialog;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    int internet;

    //Swipe load
    private SwipeRefreshLayout mSwipeRefreshLayoutEtudiant;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ///Si internet est activé alors on affiche un temps de chargement pour dl les données
        if (checkConnectivity() == 1){
            showLoadingDialog();
        }

        firstname = (TextView) view.findViewById(R.id.firstname);
        secondname = (TextView) view.findViewById(R.id.secondname);
        mail = (TextView) view.findViewById(R.id.mail);
        phoneNumber = (TextView) view.findViewById(R.id.phoneNumber);

        WebServiceUserClient.getInstance().requestUsers(this);
        WebServiceUserClient.getInstance().getUsers();

        floatingActionButtonEdited = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.floatingActionButtonEdited);
        floatingActionButtonEdited.setOnClickListener(this);


        /*****************************/
        ibCV = (ImageButton) view.findViewById(R.id.ibCV);
        ibLM = (ImageButton) view.findViewById(R.id.ibLM);
        ibEtiquette = (ImageButton) view.findViewById(R.id.ibEtiquette);
        ibDB = (ImageButton) view.findViewById(R.id.ibDB);

        ibCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooserCV();
                Toast.makeText(getActivity(),"Selectionnez le CV a envoyer",Toast.LENGTH_SHORT).show();
            }
        });

        ibLM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooserLM();
                Toast.makeText(getActivity(),"Selectionnez la Lettre de motivation a envoyer",Toast.LENGTH_SHORT).show();
            }
        });

        ibEtiquette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooserEtiquette();
                Toast.makeText(getActivity(),"Selectionnez l'Etiquette a envoyer",Toast.LENGTH_SHORT).show();
            }
        });

        ibDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooserDB();
                Toast.makeText(getActivity(),"Selectionnez le DashBoard a envoyer",Toast.LENGTH_SHORT).show();
            }
        });

        //Swipe
        mSwipeRefreshLayoutEtudiant = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_etudiant);
        mSwipeRefreshLayoutEtudiant.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupRefreshSwipe();
                        mSwipeRefreshLayoutEtudiant.setRefreshing(false);
                    }
                }, 2500);
            }
        });

    }

    private void showFileChooserCV() {
        Intent intent = new Intent();
        //Définit le fichier de sélection sur tous les types de fichiers
        intent.setType("*/*");
        //Permet de sélectionner les données et de les retourner
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Démarre une nouvelle activité pour sélectionner les fichiers et retourner les données
        startActivityForResult(Intent.createChooser(intent,"Choisissez le fichier à envoyer.."),PICK_FILE_REQUEST_CV);
    }
    private void showFileChooserLM() {
        Intent intent = new Intent();
        //Définit le fichier de sélection sur tous les types de fichiers
        intent.setType("*/*");
        //Permet de sélectionner les données et de les retourner
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Démarre une nouvelle activité pour sélectionner les fichiers et retourner les données
        startActivityForResult(Intent.createChooser(intent,"Choisissez le fichier à envoyer.."),PICK_FILE_REQUEST_LM);
    }
    private void showFileChooserEtiquette() {
        Intent intent = new Intent();
        //Définit le fichier de sélection sur tous les types de fichiers
        intent.setType("*/*");
        //Permet de sélectionner les données et de les retourner
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Démarre une nouvelle activité pour sélectionner les fichiers et retourner les données
        startActivityForResult(Intent.createChooser(intent,"Choisissez le fichier à envoyer.."),PICK_FILE_REQUEST_ETIQUETTE);
    }
    private void showFileChooserDB() {
        Intent intent = new Intent();
        //Définit le fichier de sélection sur tous les types de fichiers
        intent.setType("*/*");
        //Permet de sélectionner les données et de les retourner
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Démarre une nouvelle activité pour sélectionner les fichiers et retourner les données
        startActivityForResult(Intent.createChooser(intent,"Choisissez le fichier à envoyer.."),PICK_FILE_REQUEST_DB);
    }

    //Méthode qui récupère les nouvelles données s'il y en a
    private void setupRefreshSwipe(){
        WebServiceUserClient.getInstance().requestUsers(this);
        WebServiceUserClient.getInstance().getUsers();
    }

    @Override
    public void onClick(View view) {
        if(view == floatingActionButtonEdited){
            //Intent edtitIntent = new Intent(getActivity(), EditProfilActivity.class);
            //startActivity(edtitIntent);
        }
    }

    @Override
    public void onUsersReceived(ArrayList<User> users) {
        user = users.get(0);
        user = WebServiceUserClient.getInstance().getUserById(user.getId());
        firstname.setText(user.getNom());
        secondname.setText(user.getPrenom());
        mail.setText(user.getMail());
        phoneNumber.setText(user.getTelephone());
        //Récuperation des DOCUMENT, puis on renvoi la boîte de dialogue de progression
        loadingDialog.dismiss();

    }

    @Override
    public void onUsersFailed(String error) {

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
            Toast.makeText(getActivity(), "Aucune connexion réseau.", Toast.LENGTH_SHORT).show();
            enabled = false;
        } else {
            //Le réseau est connecté
            Log.d("Internet", "ON");
            internet = 1;
        }
        return internet;
    }
}
