package com.imerir.bouillon.areapp.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imerir.bouillon.areapp.Models.Offer;
import com.imerir.bouillon.areapp.R;

import java.util.ArrayList;

/**
 * Created by maxime on 09/03/2017.
 */

/**
 * @author Bouillon Maxime
 * @version 0.9
 * Classe gérant l'adapteur des données de la liste des offres présente sur le OffersListFragment
 */
public class OffersListAdapter extends RecyclerView.Adapter<OffersListAdapter.OfferViewHolder> {

    private ArrayList<Offer> offres;
    CardView cardView;
    private OnOfferClickListener listener;

    /**
     *
     * @param offers
     * @param listener
     */
    public OffersListAdapter(ArrayList<Offer> offers, OnOfferClickListener listener ) {
        this.offres = offers;
        this.listener = listener;
    }


    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, parent, false);
        return new OfferViewHolder(v);
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
        holder.udpate(offres.get(position));
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        int a;
        if(offres != null && !offres.isEmpty()) {a = offres.size();}
        else {a = 0;}
        return a;
    }

    /**
     *
     */
    public class OfferViewHolder extends RecyclerView.ViewHolder {
        TextView titre;
        TextView duree;
        TextView datePublication;
        TextView nomEntreprise;
        TextView lieu;

        Offer offer;

        /**
         *
         * @param itemView
         */
        public OfferViewHolder(View itemView) {
            super(itemView);

            cardView    = (CardView) itemView.findViewById(R.id.cardView);
            titre = (TextView) itemView.findViewById(R.id.Titre);
            duree = (TextView) itemView.findViewById(R.id.Durée);
            datePublication = (TextView) itemView.findViewById(R.id.DatePublication);
            nomEntreprise = (TextView) itemView.findViewById(R.id.NomEntreprise);
            lieu = (TextView) itemView.findViewById(R.id.Lieu);
        }

        /**
         *
         * @param mOffer
         */
        public void udpate(final Offer mOffer) {
            offer = mOffer;
            titre.setText(offer.getTitre());
            duree.setText(offer.getDureeContrat());
            datePublication.setText(offer.getDatePublicaiton());
            nomEntreprise.setText(offer.getNomEntreprise());
            lieu.setText(offer.getLieuEntreprise());
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onOfferClicked(mOffer);
                }
            });
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onOfferLongClicked(mOffer);
                    return true;
                }
            });
        }
    }

    /**
     *  Interface des methodes pour le clic d'une offre
     */
    public interface OnOfferClickListener {
        void onOfferClicked(Offer offre);
        void onOfferLongClicked(Offer offre);
    }
}


