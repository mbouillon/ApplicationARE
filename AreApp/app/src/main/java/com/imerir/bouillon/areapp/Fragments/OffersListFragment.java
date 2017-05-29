package com.imerir.bouillon.areapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imerir.bouillon.areapp.Activities.AddMessageActivity;
import com.imerir.bouillon.areapp.Activities.AddOfferActivity;
import com.imerir.bouillon.areapp.Activities.EditOfferActivity;
import com.imerir.bouillon.areapp.Activities.LoginActivity;
import com.imerir.bouillon.areapp.Activities.MainActivity;
import com.imerir.bouillon.areapp.Activities.MessagesListActivity;
import com.imerir.bouillon.areapp.Activities.OfferDetailActivity;
import com.imerir.bouillon.areapp.Activities.RegisterResponsableActivity;
import com.imerir.bouillon.areapp.Adapters.MessagesAdapter;
import com.imerir.bouillon.areapp.Adapters.OffersListAdapter;
import com.imerir.bouillon.areapp.Clients.WebServiceMessageClient;
import com.imerir.bouillon.areapp.Clients.WebServiceOfferClient;
import com.imerir.bouillon.areapp.Clients.WebServiceUserClient;
import com.imerir.bouillon.areapp.Models.Offer;
import com.imerir.bouillon.areapp.Models.User;
import com.imerir.bouillon.areapp.Models.WelcomeMessage;
import com.imerir.bouillon.areapp.R;

import java.util.ArrayList;
import java.util.Collections;


public class OffersListFragment extends Fragment implements View.OnClickListener, WebServiceOfferClient.OnOffersListListener,WebServiceMessageClient.OnMessagesListListener, OffersListAdapter.OnOfferClickListener, WebServiceUserClient.OnUsersListListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offers_list, null);
    }

    private TextView tvMessage;
    private ImageView ImageMessage;
    RecyclerView offersList;
    private TextView publisherName;
    WelcomeMessage message;
    private CardView cardView;

    Offer offer;
    User user;
    //Gestion de la Progress Bar des données
    ProgressDialog loadingDialog;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    int internet;

    //SwipeRefreshLayout Offres
    private SwipeRefreshLayout mSwipeRefreshLayoutOffre;

    com.github.clans.fab.FloatingActionMenu fabMenu;
    com.github.clans.fab.FloatingActionButton fabAddOffer;
    com.github.clans.fab.FloatingActionButton fabAddMessage;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //Si internet activé Affiche un temps de chargement pour dl les données
        if (checkConnectivity() == 1){
            showLoadingDialog();
        }

        WebServiceUserClient.getInstance().requestUsers(this);
        WebServiceOfferClient.getInstance().requestOffers(this);

        //Correction du bug lié au commit eda353f
        try {
            Thread.sleep(500);
            WebServiceMessageClient.getInstance().requestMessages(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        offersList = (RecyclerView) view.findViewById(R.id.offersList);
        offersList.setLayoutManager(new LinearLayoutManager(getContext()));
        tvMessage = (TextView) view.findViewById(R.id.MessageAccueil);
        WebServiceOfferClient.getInstance().getOffers();
        publisherName = (TextView) view.findViewById(R.id.PublishName);
        ImageMessage = (ImageView) view.findViewById(R.id.ImageMessage);
        cardView = (CardView) view.findViewById(R.id.cardView);
        fabMenu = (com.github.clans.fab.FloatingActionMenu) view.findViewById(R.id.fbaMenuOffers);
        fabAddMessage = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.fbaAddMessage);
        fabAddOffer = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.fbaAddOffer);
        fabAddMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMessageActivity.class);
                startActivity(intent);
            }
        });

        fabAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddOfferActivity.class);
                startActivity(intent);
            }
        });

        //Clic sur l'espace message affiche la liste de tous les messages publiés
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messagesListIntent = new Intent(getActivity(), MessagesListActivity.class);
                startActivity(messagesListIntent);
            }
        });

        //Swipe
        mSwipeRefreshLayoutOffre = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_offers);
        mSwipeRefreshLayoutOffre.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupRefreshSwipe();
                        mSwipeRefreshLayoutOffre.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        if(preferences.getBoolean("type", false) == false ){
            fabMenu.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onOfferClicked(Offer offre) {
        Intent intent = new Intent(getActivity(), OfferDetailActivity.class);
        intent.putExtra("offre_id", offre.getOfferID());
        startActivity(intent);
    }

    public void onOfferLongClicked(final Offer offre) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(preferences.getBoolean("type", false) == false){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(offre.getTitre());
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ms_offers_list_alert_edit),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Intent intent = new Intent(getActivity(), EditOfferActivity.class);
                            intent.putExtra("offre_id", offre.getOfferID());
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(getString(R.string.ms_offers_list_alert_remove),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            //Envoie la requete de suppression + Destruction de l'activité + Rechargement de la main activity
                            WebServiceOfferClient.getInstance().DELETEOffer(offre.getOfferID());
                            Toast.makeText(getActivity(), getString(R.string.ms_offer_delete_offer) + " " + offre.getTitre() + " " + getString(R.string.ms_offer_delete_delete), Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNeutralButton(getString(R.string.ms_callDialog_stop),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }
        else{

        }

    }

    @Override
    public void onMessagesFailed(String error) {

    }

    @Override
    public void onOffersReceived(ArrayList<Offer> offers) {
        Collections.reverse(offers);
        offersList.setAdapter(new OffersListAdapter(offers,this));
        //Récuperation des OFFRES, puis on renvoi la boîte de dialogue de progression
        loadingDialog.dismiss();
    }

    @Override
    public void onMessagesReceived(ArrayList<WelcomeMessage> messages) {
        message = messages.get(0);
        user = WebServiceUserClient.getInstance().getUserById(message.getPublisherId());
        tvMessage.setText(message.getMessage());
        publisherName.setText(getString(R.string.text_post_by) + " " + user.getNom() + " " + user.getPrenom());

    }

    @Override
    public void onOffersFailed(String error) {

    }

    @Override
    public void onUsersReceived(ArrayList<User> users) {

    }

    @Override
    public void onUsersFailed(String error) {

    }

    //Méthode qui récupère les nouvelles données s'il y en a
    private void setupRefreshSwipe(){
        WebServiceUserClient.getInstance().requestUsers(this);
        WebServiceOfferClient.getInstance().requestOffers(this);
    }

    //Gestion du Progress Dialog
    public void showLoadingDialog(){
        try{
            //Création d'un ProgressDialog et l'afficher
            loadingDialog = ProgressDialog.show(getActivity(), "", getString(R.string.ms_loaddingDialog_two), true, false);
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

    //Verification de la connexion internet du téléphone
    private int checkConnectivity() {
        boolean enabled = true;
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        //Vérification si le téléphone est connecté a un réseau mobile, wifi ou pas
        if ((info == null || !info.isConnected() || !info.isAvailable())) {
            //Aucune connexion internet
            internet = 0;
            Log.d("Internet", "OFF");
            Toast.makeText(getActivity(), getString(R.string.ms_error_network), Toast.LENGTH_SHORT).show();
            enabled = false;
        } else {
            //Le réseau est connecté
            Log.d("Internet", "ON");
            internet = 1;
        }
        return internet;
    }
}
