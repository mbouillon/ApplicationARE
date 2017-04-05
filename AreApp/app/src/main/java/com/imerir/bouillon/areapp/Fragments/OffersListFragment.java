package com.imerir.bouillon.areapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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


    User user;

    com.github.clans.fab.FloatingActionMenu famAddOffer;
    com.github.clans.fab.FloatingActionButton fabAddOffer;
    com.github.clans.fab.FloatingActionButton fabAddMessage;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        WebServiceUserClient.getInstance().requestUsers(this);


        WebServiceOfferClient.getInstance().requestOffers(this);

        //Correction du bug lié au commit eda353f
        try {
            Thread.sleep(200);
            WebServiceMessageClient.getInstance().requestMessages(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        offersList  =   (RecyclerView) view.findViewById(R.id.offersList);
        offersList.setLayoutManager(new LinearLayoutManager(getContext()));
        tvMessage = (TextView) view.findViewById(R.id.MessageAccueil);
        WebServiceOfferClient.getInstance().getOffers();
        publisherName = (TextView) view.findViewById(R.id.PublishName);
        ImageMessage = (ImageView) view.findViewById(R.id.ImageMessage);
        cardView = (CardView) view.findViewById(R.id.cardView);

        //Clic sur l'espace message affiche la liste de tous les messages publiés
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messagesListIntent = new Intent(getActivity(), MessagesListActivity.class);
                startActivity(messagesListIntent);
            }
        });
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


    @Override
    public void onMessagesFailed(String error) {

    }

    @Override
    public void onOffersReceived(ArrayList<Offer> offers) {
        offersList.setAdapter(new OffersListAdapter(offers,this));
    }

    @Override
    public void onMessagesReceived(ArrayList<WelcomeMessage> messages) {
        message = messages.get(0);
        user = WebServiceUserClient.getInstance().getUserById(message.getPublisherId());
        tvMessage.setText(message.getMessage());

        publisherName.setText("Publié par : " + user.getNom() + " " + user.getPrenom());

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
}
