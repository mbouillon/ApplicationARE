package com.imerir.bouillon.areapp.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.imerir.bouillon.areapp.Clients.WebServiceOfferClient;
import com.imerir.bouillon.areapp.Models.Offer;
import com.imerir.bouillon.areapp.Models.User;
import com.imerir.bouillon.areapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditOfferActivity extends AppCompatActivity {

    //Var Formation et var TypeContrat pour compatibilité BDD
    private int _formation, _typeContrat;

    private EditText nomOffre, duree, nomEntreprise, lieu, nomContact, mailContact, telephoneContact, details, detailsResponsable;
    private Spinner typeContrat, formation;
    private Button ajouterOffre;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        mToolbar.inflateMenu(R.menu.main_menu);
        mToolbar.setTitle(getResources().getText(R.string.title_add_offer));
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Cast des EditText
        nomOffre = (EditText) findViewById(R.id.NomOffre);
        duree = (EditText) findViewById(R.id.Duree);
        nomEntreprise = (EditText) findViewById(R.id.NomEntreprise);
        lieu = (EditText) findViewById(R.id.Lieu);
        nomContact = (EditText) findViewById(R.id.NomContact);
        mailContact = (EditText) findViewById(R.id.MailContact);
        telephoneContact = (EditText) findViewById(R.id.PhoneContact);
        details = (EditText) findViewById(R.id.Detail);
        detailsResponsable = (EditText) findViewById(R.id.DetailsResponsables);

        //Spinners + Adapters
        //Spinner contrat
        typeContrat = (Spinner) findViewById(R.id.typeDeContrat);
        ArrayAdapter<CharSequence> spinnerAdapterContrat = ArrayAdapter.createFromResource(this, R.array.liste_contrats, android.R.layout.simple_spinner_item);
        spinnerAdapterContrat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeContrat.setAdapter(spinnerAdapterContrat);
        //Spinner formation
        formation = (Spinner) findViewById(R.id.formation);
        ArrayAdapter<CharSequence> spinnerAdapterFormation = ArrayAdapter.createFromResource(this, R.array.liste_formations, android.R.layout.simple_spinner_item);
        spinnerAdapterFormation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        formation.setAdapter(spinnerAdapterFormation);

        // Récuperation de l'arrayList passée en paramètre de l'intent pour eviter un appel serveur
        Bundle extras = getIntent().getExtras();
        Offer offre = (Offer) extras.getSerializable("offre");

        nomOffre.setText(offre.getTitre());
        duree.setText(offre.getDureeContrat());
        nomEntreprise.setText(offre.getNomEntreprise());
        lieu.setText(offre.getLieuEntreprise());
        nomContact.setText(offre.getNomContact());
        mailContact.setText(offre.getMailContact());
        telephoneContact.setText(offre.getTelephoneContact());
        details.setText(offre.getDetails());
        detailsResponsable.setText(offre.getDetailsResponsables());

        //Bouton envoyer offre
        ajouterOffre = (Button) findViewById(R.id.AjouterOffre);

        //Bouton publier offre
        ajouterOffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gestion des spinners + Langue
                switch (typeContrat.getSelectedItem().toString()) {
                    case "Stage":
                        _typeContrat = 1;
                        break;
                    case "Apprentissage":
                        _typeContrat = 2;
                        break;
                    case "Type non connu":
                        _typeContrat = 3;
                        break;
                    case "Internship":
                        _typeContrat = 1;
                        break;
                    case "Apprenticeship":
                        _typeContrat = 2;
                        break;
                    case "Type not known":
                        _typeContrat = 1;
                        break;
                    default:
                        _typeContrat = 0;
                }

                switch (formation.getSelectedItem().toString()) {
                    case "CDPIR":
                        _formation = 1;
                        break;
                    case "CDSM":
                        _formation = 2;
                        break;
                    case "UPVD":
                        _formation = 3;
                        break;
                    default:
                        _formation = 0;
                }

                /*
                    Si champs pas remplis afficher message erreur,
                    Sinon Si type contrat non rempli afficher message d'erreur
                    Sinon Si formation non rempli afficher avertissement
                    Si avertissement oui envoyer le POST
                    Si avertissement non ne pas envoyer POST
                    Si tous champs OK envoyer POST
                 */
                if (nomOffre.getText().toString().isEmpty() || duree.getText().toString().isEmpty() || nomContact.getText().toString().isEmpty() || lieu.getText().toString().isEmpty() || mailContact.getText().toString().isEmpty() || telephoneContact.getText().toString().isEmpty() || nomEntreprise.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.ms_error_offer), Toast.LENGTH_SHORT).show();
                } else if (_typeContrat == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.ms_error_offer_contrat), Toast.LENGTH_SHORT).show();
                } else if (_formation == 0) {
                    new AlertDialog.Builder(EditOfferActivity.this)
                            .setTitle(getString(R.string.ms_warning))
                            .setMessage(getString(R.string.ms_error_offer_formation))
                            .setPositiveButton(getString(R.string.ms_yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    _castAndSendOffer();
                                }
                            })
                            .setNegativeButton(getString(R.string.ms_no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                } else {
                    _castAndSendOffer();
                }
                //Fin du onClick du bouton ajouterOffre
            }
        });
    }


    //Code utilisé 2 fois dans le on click (Si user ne renseigne pas formation et si il le renseigne)
    //Donc je fait une methode pour pas surcharger le onClick()
    private void _castAndSendOffer(){
        //Creation de l'objet json qui va servir a la creation de l'objet Offer
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("Titre", nomOffre.getText().toString());
            jsonObject.put("Duree", duree.getText().toString());
            jsonObject.put("FormationAssociee", _formation);
            jsonObject.put("Contrat", _typeContrat);
            jsonObject.put("NomContact", nomContact.getText().toString());
            jsonObject.put("Lieu", lieu.getText().toString());
            jsonObject.put("ContactMail", mailContact.getText().toString());
            jsonObject.put("ContactPhone", telephoneContact.getText().toString());
            jsonObject.put("NomEntreprise", nomEntreprise.getText().toString());
            jsonObject.put("Details", details.getText().toString());
            jsonObject.put("DetailsResponsable", detailsResponsable.getText().toString());
            //Création de l'objet json et POST dans la BDD + Après le POST desctruction de l'activité et rechargement de l'activité principale
            Offer offre = new Offer(jsonObject);
            WebServiceOfferClient.getInstance().PUTOffer(offre);
            Toast.makeText(getApplicationContext(),getString(R.string.ms_succes_offer) + " " + nomOffre.getText().toString() + " " + getString(R.string.ms_succes_offer_add), Toast.LENGTH_LONG).show();
            Intent MainActivityIntent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(MainActivityIntent);
            finish();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }



    //fin de la classe
}
