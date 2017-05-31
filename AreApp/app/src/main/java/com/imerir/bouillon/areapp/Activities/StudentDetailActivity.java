package com.imerir.bouillon.areapp.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.imerir.bouillon.areapp.Clients.WebServiceUserClient;
import com.imerir.bouillon.areapp.Models.Document;
import com.imerir.bouillon.areapp.Models.User;
import com.imerir.bouillon.areapp.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

/**
 * Created by SireRemy on 31/05/2017.
 */

public class StudentDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //Permission appel telephone
    private final static int CALL_PHONE_PERMISSION = 1;

    //Declatation des widgets
    private Toolbar mToolbar;

    TextView firstname, secondname, mail, phoneNumber;

    TextView tvCV_load, tvLM_load, tvEtiquette_load, tvDB_load;
    ImageButton ibCV, ibLM, ibEtiquette, ibDB;

    com.github.clans.fab.FloatingActionButton callBtn, mailBtn;

    User user;
    Document document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        user = WebServiceUserClient.getInstance().getUserById(getIntent().getIntExtra("user_id", 0));

        //Toolbar + action flêche retour
        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(user.getNom() + " " + user.getPrenom());
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Cast des widgets
        firstname = (TextView) findViewById(R.id.firstname);
        secondname = (TextView) findViewById(R.id.secondname);
        mail = (TextView) findViewById(R.id.mail);
        phoneNumber = (TextView) findViewById(R.id.phoneNumber);

        //Cast des image Button
        ibCV = (ImageButton) findViewById(R.id.ibCV);
        ibLM = (ImageButton) findViewById(R.id.ibLM);
        ibEtiquette = (ImageButton) findViewById(R.id.ibEtiquette);
        ibDB = (ImageButton) findViewById(R.id.ibDB);

        ibCV.setOnClickListener(this);
        ibLM.setOnClickListener(this);
        ibEtiquette.setOnClickListener(this);
        ibDB.setOnClickListener(this);

        //Recuperation des données de l'offre cliquée + Affichage sur le widget
        firstname.setText(user.getNom());
        secondname.setText(user.getPrenom());
        mail.setText(user.getMail());
        phoneNumber.setText(user.getTelephone());

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
        else if(v==mailBtn) {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.email");
            startActivity(intent);
        }
        if(v==ibCV){
            Log.d("Ok", "Pdf open");
            Toast.makeText(getApplication(), "OK : PDF CV", Toast.LENGTH_SHORT).show();
        }
        if(v==ibLM){
            Log.d("Ok", "Pdf open");
            Toast.makeText(getApplication(), "OK : PDF LM", Toast.LENGTH_SHORT).show();
        }
        if(v==ibEtiquette){
            Log.d("Ok", "Pdf open");
            Toast.makeText(getApplication(), "OK : PDF Etiquette", Toast.LENGTH_SHORT).show();
        }
        if(v==ibDB){
            Log.d("Ok", "Pdf open");
            Toast.makeText(getApplication(), "OK : PDF DB", Toast.LENGTH_SHORT).show();
        }
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
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_student_detail, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteStudent:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.md_remove_student))
                        .setMessage(getString(R.string.ms_offer_delete_ask))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Pouvoir supprimer l'etudiant
                                //deleteStudent();
                                //Toast.makeText(getApplicationContext(), "Suppression de l'entrée...", Toast.LENGTH_LONG).show();
                                //onBackPressed();
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

    /**
     * Alerte pour passer un appel telephonique
     */
    public void showCallDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.ms_callDIalog_call_ask)).setMessage(getString(R.string.ms_callDialog_call_contact) + " " + user.getNom() + " " + user.getPrenom() + " " + getString(R.string.ms_callDialog_call_end))
                .setPositiveButton(getString(R.string.ms_callDialog_call), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(getString(R.string.ms_callDialog_phone) + " " + user.getTelephone()));
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
}
