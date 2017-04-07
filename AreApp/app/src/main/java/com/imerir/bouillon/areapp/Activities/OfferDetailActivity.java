package com.imerir.bouillon.areapp.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
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

import com.imerir.bouillon.areapp.Clients.WebServiceOfferClient;
import com.imerir.bouillon.areapp.Models.Offer;
import com.imerir.bouillon.areapp.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

public class OfferDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //Permission appel telephone
    private final static int CALL_PHONE_PERMISSION = 1;

    //Declatation des widgets
    private Toolbar mToolbar;

    TextView Titre, Duree, NomEntreprise, Lieu, NomContact, Mail, Telephone, DatePublish;
    TextView Details, DetailsResponsables;

    com.github.clans.fab.FloatingActionButton callBtn, mailBtn;

    Offer offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

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

        Details =   (TextView) findViewById(R.id.tvDetails);
        DetailsResponsables =   (TextView) findViewById(R.id.tvDetailsResponsables);

        //Recuperation des données de l'offre cliquée + Affichage sur le widget
        Titre.setText(offer.getTitre());
        Duree.setText(offer.getDureeContrat());
        NomEntreprise.setText(offer.getNomEntreprise());
        Lieu.setText(offer.getLieuEntreprise());
        NomContact.setText(offer.getNomContact());
        Mail.setText(offer.getMailContact());
        Telephone.setText(offer.getTelephoneContact());
        DatePublish.setText("Publiée le : " + offer.getDatePublicaiton());

        Details.setText(offer.getDetails());
        DetailsResponsables.setText(offer.getDetailsResponsables());

        //Boutons flottats + Cast + Définition de l'icône.
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

    //On Click des deux boutons flottants
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

    public void showCallDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Appeler ?").setMessage("Voulez-vous appeler " + offer.getNomContact() + " ?")
                .setPositiveButton("Appeler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + offer.getTelephoneContact()));
                        try {
                            startActivity(intent);
                        }
                        catch(SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    //Demande la permission d'utiliser le telephone
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_offer_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteOffer:
                //Pouvoir supprimer l'offre en cours
                //deleteOffer();
                //Toast.makeText(getApplicationContext(), "Suppression...", Toast.LENGTH_LONG).show();
                //super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
