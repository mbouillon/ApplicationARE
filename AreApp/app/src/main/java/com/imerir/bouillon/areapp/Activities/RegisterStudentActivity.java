package com.imerir.bouillon.areapp.Activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.opengl.EGLSurface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.imerir.bouillon.areapp.Clients.WebServiceUserClient;
import com.imerir.bouillon.areapp.Models.User;
import com.imerir.bouillon.areapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * @author Bouillon Maxime
 * @version 0.9
 * Classe gérant l'activité proposant l'inscription d'un étudiant
 */
public class RegisterStudentActivity extends AppCompatActivity implements WebServiceUserClient.OnUsersListListener {


    private EditText etName;
    private EditText etfName;
    private EditText etPhoneNumber;
    private RadioGroup radioButtonGroup;
    private Button bRegister;

    private Toolbar mToolbar;

    private int formation = 1;
    private boolean userExists = false;

    /**
     *
     * @param savedInstanceState
     * Actions lancées a la création de l'activité principalement la mise en place de l'IHM
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        Bundle extras = getIntent().getExtras();

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);

        final ActionBar ab = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        mToolbar.inflateMenu(R.menu.main_menu);
        mToolbar.setTitle(getResources().getText(R.string.title_register_student));
        mToolbar.setTitleTextColor(Color.WHITE);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //EditText
        etName = (EditText) findViewById(R.id.etNameR);
        etfName = (EditText) findViewById(R.id.etfNameR);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumberR);
        //RadioGroup
        radioButtonGroup = (RadioGroup) findViewById(R.id.radioButtonGroup);

        //Gestion du RadioGroup
        radioButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                //Trouve le bouton radio sélectionné par ID retoruné
                if(checkedId == R.id.cbCDPIR){
                    formation = 1;
                }else if (checkedId == R.id.cbCDSM){
                    formation = 2;
                }else if (checkedId == R.id.cbUPVD){
                    formation = 3;
                }
            }
        });

        etName.setText(extras.getString("name"));
        etfName.setText(extras.getString("famName"));

        //Gestion du bouton bRegister
        bRegister = (Button) findViewById(R.id.bRegisterR);
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle extras = getIntent().getExtras();

                ArrayList<User> users = (ArrayList<User>) extras.getSerializable("userArray");

                //Save la couleur par defaut de la couleur du texte d'un champ pour set la couleur par défaut dans la gestion des erreurs
                ColorStateList oldColors =  etName.getTextColors();

                //Creation d'un objet en JSON pour le POST Serveur afin de construire le modèle USER
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Nom", etName.getText().toString());
                    jsonObject.put("Prenom", etfName.getText().toString());
                    jsonObject.put("Mail", extras.getString("mail").toString());
                    jsonObject.put("Telephone", etPhoneNumber.getText().toString());
                    jsonObject.put("type", true);
                    jsonObject.put("Formation", formation);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (etName.getText().toString().isEmpty() || etfName.getText().toString().isEmpty() || etPhoneNumber.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterStudentActivity.this, getResources().getText(R.string.error_empty_fields), Toast.LENGTH_LONG).show();
                }
                else {
                    User user = new User(jsonObject);
                    WebServiceUserClient.getInstance().POSTUser(user);
                    Toast.makeText(RegisterStudentActivity.this, getResources().getText(R.string.account_created_successfuly), Toast.LENGTH_SHORT).show();
                    Intent LoginActivityIntent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(LoginActivityIntent);
                    finish();
                }
            }
        });
    }

    /**
     *
     * @param users
     */
    @Override
    public void onUsersReceived(ArrayList<User> users) {

    }

    /**
     *
     * @param error
     */
    @Override
    public void onUsersFailed(String error) {

    }
}
