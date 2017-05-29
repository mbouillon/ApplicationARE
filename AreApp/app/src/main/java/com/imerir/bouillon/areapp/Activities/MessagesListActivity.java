package com.imerir.bouillon.areapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.imerir.bouillon.areapp.Adapters.MessagesAdapter;
import com.imerir.bouillon.areapp.Adapters.OffersListAdapter;
import com.imerir.bouillon.areapp.Clients.WebServiceMessageClient;
import com.imerir.bouillon.areapp.Clients.WebServiceOfferClient;
import com.imerir.bouillon.areapp.Clients.WebServiceUserClient;
import com.imerir.bouillon.areapp.Models.WelcomeMessage;
import com.imerir.bouillon.areapp.R;

import java.util.ArrayList;
import java.util.Collections;

public class MessagesListActivity extends AppCompatActivity implements WebServiceMessageClient.OnMessagesListListener, MessagesAdapter.OnMessageClickListener {


    RecyclerView recyclerView;

    ArrayList<WelcomeMessage> _message;

    //Gestion de la Progress Bar
    ProgressDialog loadingDialog;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private Response.Listener listener;
    int internet;

    //Toolbar
    private Toolbar mToolbar;

    //SwipeRefreshLayout Message
    private SwipeRefreshLayout mSwipeRefreshLayoutMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);

        //Si internet est activé alors on affiche un temps de chargement pour dl les données
        if (checkConnectivity() == 1){
            showLoadingDialog();
        }

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
        getSupportActionBar().setTitle(getResources().getText(R.string.title_message));
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Swipe
        mSwipeRefreshLayoutMessage = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_message);
        mSwipeRefreshLayoutMessage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupRefreshSwipe();
                        mSwipeRefreshLayoutMessage.setRefreshing(false);
                    }
                }, 2500);
            }
        });
    }

    @Override
    public void onMessageClicked(WelcomeMessage message) {

    }

    @Override
    public void onMessageLongClicked(final WelcomeMessage message) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getBoolean("type", false) == false){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getString(R.string.iv_message));
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ms_offers_list_alert_remove),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            //Envoie la requete de suppression + Destruction de l'activité + Rechargement de la main activity
                            WebServiceMessageClient.getInstance().DELETEMessage(message.getId());
                            Toast.makeText(getApplicationContext(), getString(R.string.iv_message_id) + " " + message.getId() + " " + getString(R.string.ms_offer_delete_delete), Toast.LENGTH_LONG).show();
                            setupRefreshSwipe();
                        }
                    })
                    .setNegativeButton(R.string.ms_callDialog_stop,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }else{

        }
    }

    @Override
    public void onMessagesReceived(ArrayList<WelcomeMessage> messages) {
        Collections.reverse(messages);
        recyclerView.setAdapter(new MessagesAdapter(messages, this));
        //Récuperation des MESSAGES, puis on renvoi la boîte de dialogue de progression
        loadingDialog.dismiss();
    }

    @Override
    public void onMessagesFailed(String error) {

    }

    //Méthode qui récupère les nouvelles données s'il y en a
    private void setupRefreshSwipe(){
        WebServiceMessageClient.getInstance().requestAllMessages(this);
    }

    //Gestion du Progress Dialog
    public void showLoadingDialog() {
        try {
            //Création d'un ProgressDialog et l'afficher
            loadingDialog = ProgressDialog.show(this, "", getString(R.string.ms_loaddingDialog_two), true, false);
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

    //Verification de la connexion internet du téléphone
    private int checkConnectivity() {
        boolean enabled = true;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        //Vérification si le téléphone est connecté a un réseau mobile, wifi ou pas
        if ((info == null || !info.isConnected() || !info.isAvailable())) {
            //Aucune connexion internet
            internet = 0;
            Log.d("Internet", "OFF");
            Toast.makeText(getApplicationContext(), getString(R.string.ms_error_network), Toast.LENGTH_SHORT).show();
            enabled = false;
        } else {
            //Le réseau est connecté
            Log.d("Internet", "ON");
            internet = 1;
        }
        return internet;
    }
}
