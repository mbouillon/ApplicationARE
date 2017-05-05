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
import com.imerir.bouillon.areapp.R;

import org.json.JSONException;
import org.json.JSONObject;


public class AddOfferActivity extends AppCompatActivity {

    //Var Formation et var TypeContrat pour compatibilité BDD
    private int _formation, _typeContrat;

    private EditText nomOffre, duree, nomEntreprise, lieu, nomContact, mailContact, telephoneContact;
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
        mToolbar.setTitle("Ajouter une nouvelle offre");
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
        nomEntreprise = (EditText) findViewById(R.id.NomEntreprise);

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

        //Bouton envoyer offre
        ajouterOffre = (Button) findViewById(R.id.AjouterOffre);

        //Bouton publier offre
        ajouterOffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gestion des spinners
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

                //VOIR COMMENT ALLEGER CE PLAT DE SPAGUETTI
                /*
                    Si champs pas remplis afficher message erreur,
                    Sinon Si type contrat non rempli afficher message d'erreur
                    Sinon Si formation non rempli afficher avertissement
                    Si avertissement oui envoyer le POST
                    Si avertissement non ne pas envoyer POST
                    Si tous champs OK envoyer POST
                 */
                if (nomOffre.getText().toString().equals("") & duree.getText().toString().equals("") & nomContact.getText().toString().equals("") & lieu.getText().toString().equals("") & mailContact.getText().toString().equals("") & telephoneContact.getText().toString().equals("") & nomEntreprise.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Attention, veuillez renseigner tous les champs textes afin de pouvoir poster cette offre", Toast.LENGTH_SHORT).show();
                } else if (_typeContrat == 0) {
                    Toast.makeText(getApplicationContext(), "Veuillez renseigner le champ type de contrat, si il est inconnu choisissez l'option 'Type Non Connu', merci ", Toast.LENGTH_SHORT).show();
                } else if (_formation == 0) {
                    new AlertDialog.Builder(AddOfferActivity.this)
                            .setTitle("Attention")
                            .setMessage("Vous n'avez pas selectionné de formation ciblée pour cette offre, si vous laissez ce champ par défaut tous les étudiants pourront voir l'offre proposée. Voulez vous continuer ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    _castAndSendOffer();
                                }
                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
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


    //Code utilisée 2 fois dans le on click (Si user ne rensegne pas formation et si il le renseigne)
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
                 //Création de l'objet json et POST dans la BDD + Après le POST desctruction de l'activité et rechargement de l'activité principale
                 Offer offre = new Offer(jsonObject);
                 WebServiceOfferClient.getInstance().POSTOffer(offre);
                 Toast.makeText(getApplicationContext(), "L'offre " + nomOffre.getText().toString() + " a bien été ajoutée", Toast.LENGTH_LONG).show();
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
