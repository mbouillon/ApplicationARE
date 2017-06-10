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

import static com.imerir.bouillon.areapp.Utils.App.addChar;

/**
 * Created by SireRemy on 31/05/2017.
 */

public class StudentDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //Permission appel telephone
    private final static int CALL_PHONE_PERMISSION = 1;

    //Declatation des widgets
    private Toolbar mToolbar;

    TextView name, formation, cv, lm, etiquette, dashboard, telephone;

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
        name = (TextView) findViewById(R.id.firstname);
        formation = (TextView) findViewById(R.id.formation);
        cv = (TextView) findViewById(R.id.cv);
        lm = (TextView) findViewById(R.id.lm) ;
        etiquette = (TextView) findViewById(R.id.etiquette);
        dashboard = (TextView) findViewById(R.id.dashboard);
        telephone = (TextView) findViewById(R.id.telephone);


        name.setText(user.getPrenom().toString() + " " + user.getNom().toString().toUpperCase());
        String tel;
        tel = addChar(user.getTelephone().toString(), " ", 2);
        telephone.setText(tel);
        switch (user.getFormation()) {
            case 1:
                formation.setText("CDPIR");
                break;
            case 2:
                formation.setText("CDSM");
                break;
            case 3:
                formation.setText("UPVD");
                break;
            default:
                formation.setText("CDPIR");
        }

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertNotImplemented();
            }
        });

        lm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertNotImplemented();
            }
        });

        etiquette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertNotImplemented();
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertNotImplemented();
            }
        });



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
    }

    private void showAlertNotImplemented(){
        new android.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.ms_warning))
                .setMessage(getString(R.string.alert))
                .setPositiveButton(getString(R.string.ms_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
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
