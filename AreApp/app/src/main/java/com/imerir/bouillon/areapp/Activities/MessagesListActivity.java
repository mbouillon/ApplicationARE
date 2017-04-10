package com.imerir.bouillon.areapp.Activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.Response;
import com.imerir.bouillon.areapp.Adapters.MessagesAdapter;
import com.imerir.bouillon.areapp.Clients.WebServiceMessageClient;
import com.imerir.bouillon.areapp.Models.WelcomeMessage;
import com.imerir.bouillon.areapp.R;

import java.util.ArrayList;
import java.util.Collections;

public class MessagesListActivity extends AppCompatActivity implements WebServiceMessageClient.OnMessagesListListener {


    RecyclerView recyclerView;

    ArrayList<WelcomeMessage> _message;

    private Toolbar mToolbar;

    //Gestion de la Progress Bar
    ProgressDialog loadingDialog;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private Response.Listener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);

        //Affiche un temps de chargement
        showLoadingDialogData();

        _message = new ArrayList<WelcomeMessage>();

        //Assisgnation du recycler view + WebService
        WebServiceMessageClient.getInstance().requestAllMessages(this);
        recyclerView = (RecyclerView) findViewById(R.id.messagesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        WebServiceMessageClient.getInstance().getMessages();

        //Toolbar + action flêche retour
        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Anciens Messages Publiés");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onMessagesReceived(ArrayList<WelcomeMessage> messages) {
        Collections.reverse(messages);
        recyclerView.setAdapter(new MessagesAdapter(messages, this));
        loadingDialog.dismiss();
    }

    @Override
    public void onMessagesFailed(String error) {

    }

    public void showLoadingDialogData() {
        try {
            //Création d'un ProgressDialog et l'afficher
            loadingDialog = ProgressDialog.show(this, "Veuillez patienter", "Chargement en cours...", true, false);
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
        } catch (Exception e) {

        }
    }
}
