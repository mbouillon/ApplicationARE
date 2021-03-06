package com.imerir.bouillon.areapp.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.imerir.bouillon.areapp.Clients.WebServiceOfferClient;
import com.imerir.bouillon.areapp.Models.Offer;
import com.imerir.bouillon.areapp.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;


/**
 * @author Bouillon Maxime
 * @version 0.9
 * Classe gérant l'activité proposant le détail d'une offre
 */
public class OfferDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //Permission appel telephone
    private final static int CALL_PHONE_PERMISSION = 1;

    //Declatation des widgets
    private Toolbar mToolbar;

    TextView Titre, Duree, NomEntreprise, Lieu, NomContact, Mail, Telephone, DatePublish, Contrat, tvDetailsResponsables;
    TextView Details, DetailsResponsables;

    com.github.clans.fab.FloatingActionButton callBtn, mailBtn;

    Offer offer;

    /**
     * @param savedInstanceState
     * Actions lancées a la création de l'activité principalement la mise en place de l'IHM
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        offer = WebServiceOfferClient.getInstance().getOffer(getIntent().getIntExtra("offre_id", 0));

        //Toolbar + action flêche retour
        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(offer.getTitre());
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Cast des widgets
        Titre =   (TextView) findViewById(R.id.tvTitre);
        Duree =   (TextView) findViewById(R.id.tvDuree);
        NomEntreprise =   (TextView) findViewById(R.id.tvNomEntreprise);
        Lieu =   (TextView) findViewById(R.id.tvLieu);
        NomContact =   (TextView) findViewById(R.id.tvNomContact);
        Mail =   (TextView) findViewById(R.id.tvMail);
        Telephone =   (TextView) findViewById(R.id.tvTelephone);
        DatePublish = (TextView) findViewById(R.id.tvPublishOffre);
        Contrat = (TextView) findViewById(R.id.tvContrat);
        tvDetailsResponsables = (TextView) findViewById(R.id.textDetailsResponsables);

        Details =   (TextView) findViewById(R.id.tvDetails);
        DetailsResponsables =   (TextView) findViewById(R.id.tvDetailsResponsables);

        if (preferences.getBoolean("type", false) == true){
            DetailsResponsables.setVisibility(View.INVISIBLE);
            tvDetailsResponsables.setVisibility(View.INVISIBLE);
        }

        //Recuperation des données de l'offre cliquée + Affichage sur le widget
        Titre.setText(offer.getTitre());
        Duree.setText(offer.getDureeContrat());
        NomEntreprise.setText(offer.getNomEntreprise());
        Lieu.setText(offer.getLieuEntreprise());
        NomContact.setText(offer.getNomContact());
        Mail.setText(offer.getMailContact());
        Telephone.setText(offer.getTelephoneContact());
        DatePublish.setText(getString(R.string.text_post_date) + " " + offer.getDatePublicaiton());
        switch (offer.getTypeContrat()){
            case 1:
                Contrat.setText(getString(R.string.type_Internship));
                break;
            case 2:
                Contrat.setText(R.string.type_add_offer_Apprenticeship);
                break;
            default:
                Contrat.setText(R.string.type_add_offer_Type_not_known);
        }

        Details.setText(offer.getDetails());
        DetailsResponsables.setText(offer.getDetailsResponsables());

        //Boutons flottants + Cast + Définition de l'icône.
        callBtn = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.callBtn);
        mailBtn = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.mailBtn);
        callBtn.setImageDrawable(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_phone)
                .color(Color.WHITE)
                .sizeDp(24));
        mailBtn.setImageDrawable(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_mail)
                .color(Color.WHITE)
                .sizeDp(24));

        //Listenner attend le click
        callBtn.setOnClickListener(this);
        mailBtn.setOnClickListener(this);
    }


    /**
     * On Click des deux boutons flottants
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(v==callBtn) {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                showCallDialog();
            }
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        CALL_PHONE_PERMISSION);
            }
        }
        //TODO Changer pour envoyer le message direct sans ouvrir une app externe ou permettre l'ouverture depuis toutes les app mail dispo
        else if(v==mailBtn) {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.email");
            startActivity(intent);
        }
    }

    /**
     * Alerte pour passer un appel telephonique
     */
    public void showCallDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.ms_callDIalog_call_ask)).setMessage(getString(R.string.ms_callDialog_call_contact) + " " + offer.getNomContact() + " " + getString(R.string.ms_callDialog_call_end))
                .setPositiveButton(getString(R.string.ms_callDialog_call), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(getString(R.string.ms_callDialog_phone) + " " + offer.getTelephoneContact()));
                        try {
                            startActivity(intent);
                        }
                        catch(SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.ms_callDialog_stop), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * Demande la permission d'utiliser le telephone
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CALL_PHONE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCallDialog();
                }
            }
            break;
            default:
                break;
        }
    }

    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getBoolean("type",false) == false){
            getMenuInflater().inflate(R.menu.menu_offer_detail, menu);
        }
        return true;
    }

    /**
     * Gère le menu a droite de la toolbar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteOffer:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.ms_offer_delete))
                        .setMessage(getString(R.string.ms_offer_delete_ask))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Envoie la requete de suppression + Destruction de l'activité + Rechargement de la main activity
                                WebServiceOfferClient.getInstance().DELETEOffer(offer.getOfferID());
                                Toast.makeText(getApplicationContext(), getString(R.string.ms_offer_delete_offer) + " " + offer.getTitre() + " " + getString(R.string.ms_offer_delete_delete), Toast.LENGTH_LONG).show();
                                Intent MainActivityIntent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(MainActivityIntent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
