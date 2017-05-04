package com.imerir.bouillon.areapp.Activities;

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
 * Created by maxime on 07/03/2017.
 */

public class RegisterStudentActivity extends AppCompatActivity implements View.OnClickListener, WebServiceUserClient.OnUsersListListener {

    private EditText etName;
    private EditText etfName;
    private EditText etMail;
    private EditText etConfirmMail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etPhoneNumber;
    private RadioGroup radioButtonGroup;
    private RadioButton cbCDPIR;
    private RadioButton cbCDSM;
    private RadioButton cbUPVD;
    private Button bRegister;

    private Toolbar mToolbar;

    private int formation = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Inscription étudiant");
        setContentView(R.layout.activity_register_student);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);

        final ActionBar ab = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        mToolbar.inflateMenu(R.menu.main_menu);
        mToolbar.setTitle("Inscription étudiant IMERIR ");
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
        etMail = (EditText) findViewById(R.id.etMailLoginR);
        etConfirmMail = (EditText) findViewById(R.id.etConfirmMailR);
        etPassword = (EditText) findViewById(R.id.etPasswordR);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPasswordR);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumberR);
        //RadioGroup
        radioButtonGroup = (RadioGroup) findViewById(R.id.radioButtonGroup);
        cbCDPIR = (RadioButton) findViewById(R.id.cbCDPIR) ;
        cbCDSM = (RadioButton) findViewById(R.id.cbCDSM) ;
        cbUPVD = (RadioButton) findViewById(R.id.cbUPVD) ;

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

        //Gestion du bouton bRegister
        bRegister = (Button) findViewById(R.id.bRegisterR);
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Requete JSON
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Nom", etName.getText().toString());
                    jsonObject.put("Prenom", etfName.getText().toString());
                    jsonObject.put("Mail", etMail.getText().toString());
                    jsonObject.put("Password", etPassword.getText().toString());
                    jsonObject.put("Telephone", etPhoneNumber.getText().toString());
                    jsonObject.put("Type", 1);
                    jsonObject.put("Formation", formation);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //TODO Vérifier si imerir.com
                //TODO Ne pas afficher un toast dans les erreurs de champs mais afficher le champ en rouge

                //vérification si les champs sont remplis
                if (((!etName.getText().toString().equals(""))) &
                        ((!etfName.getText().toString().equals(""))) &
                        ((!etPhoneNumber.getText().toString().equals(""))) &
                        ((!etMail.getText().toString().equals(""))) &
                        ((!etConfirmMail.getText().toString().equals(""))) &
                        ((!etPassword.getText().toString().equals(""))) &
                        ((!etConfirmPassword.getText().toString().equals("")))) {
                    Log.d("Ok", "Champs remplis");

                    //Verifie que le mail et pwd soies identique
                    if (((etMail.getText().toString()).equals(etConfirmMail.getText().toString())) && ((etPassword.getText().toString()).equals(etConfirmPassword.getText().toString()))) {
                        User user = new User(jsonObject);
                        //poste le user dans la base de données
                        WebServiceUserClient.getInstance().POSTUser(user);
                        Log.d("Ok", "Compte créé avec succès.");
                        Toast.makeText(getApplication(), "Compte créé avec succès.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.d("Erreur", "Password & Mail");
                        Toast.makeText(getApplication(), "Erreur : Mot de passe ou Email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("Ok", "Champs invalides");
                    Toast.makeText(getApplication(), "Erreur : Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //TODO Delete after tests
        etName.setText("Name");
        etfName.setText("FName");
        etMail.setText("Name60000@imerir.com");
        etConfirmMail.setText("Name60000@imerir.com");
        etPhoneNumber.setText("0657463678");
        etPassword.setText("azerty");
        etConfirmPassword.setText("azerty");
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onUsersReceived(ArrayList<User> users) {

    }

    @Override
    public void onUsersFailed(String error) {

    }
}
