package com.imerir.bouillon.areapp.Activities;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.imerir.bouillon.areapp.Clients.WebServiceUserClient;
import com.imerir.bouillon.areapp.Models.User;
import com.imerir.bouillon.areapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterResponsableActivity extends AppCompatActivity implements View.OnClickListener, WebServiceUserClient.OnUsersListListener {

    private EditText etName;
    private EditText etfName;
    private EditText etMail;
    private EditText etConfirmMail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etPhoneNumber;
    private EditText etAcessCode;
    private Button bRegister;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_responsable);


        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);

        final ActionBar ab = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        mToolbar.inflateMenu(R.menu.main_menu);
        mToolbar.setTitle("Inscription responsable ARE");
        mToolbar.setTitleTextColor(Color.WHITE);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etName = (EditText) findViewById(R.id.etNameR);
        etfName = (EditText) findViewById(R.id.etfNameR);
        etMail = (EditText) findViewById(R.id.etMailLoginR);
        etConfirmMail = (EditText) findViewById(R.id.etConfirmMailR);
        etPassword = (EditText) findViewById(R.id.etPasswordR);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPasswordR);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumberR);
        etAcessCode = (EditText) findViewById(R.id.etAcessCodeR);
        bRegister = (Button) findViewById(R.id.bRegisterR);

        bRegister.setOnClickListener(this);

        //TODO Delete after tests
        etName.setText("Name");
        etfName.setText("FName");
        etMail.setText("Name60000@imerir.com");
        etConfirmMail.setText("Name60000@imerir.com");
        etPhoneNumber.setText("0657463678");
        etPassword.setText("azerty");
        etConfirmPassword.setText("azerty");
        etAcessCode.setText("1234");
    }

    @Override
    public void onClick(View view) {
        //Requete JSON
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Nom", etName.getText().toString());
            jsonObject.put("Prenom", etfName.getText().toString());
            jsonObject.put("Mail", etMail.getText().toString());
            jsonObject.put("Password", etPassword.getText().toString());
            jsonObject.put("Telephone", etPhoneNumber.getText().toString());
            jsonObject.put("Type", 1);
            jsonObject.put("Formation", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //TODO Vérifier si imerir.com
        //TODO Ne pas afficher un toast dans les erreurs de champs mais afficher le champ en rouge
        //vérification si les champs sont remplis
        if (((!etName.getText().toString().equals(""))) &
                ((!etfName.getText().toString().equals(""))) &
                ((!etPhoneNumber.getText().toString().equals(""))) &
                ((!etAcessCode.getText().toString().equals(""))) &
                ((!etMail.getText().toString().equals(""))) &
                ((!etConfirmMail.getText().toString().equals(""))) &
                ((!etPassword.getText().toString().equals(""))) &
                ((!etConfirmPassword.getText().toString().equals("")))) {
                    Log.d("Ok", "Champs remplis");
            //Verifie que le mail et pwd sois identique
            //TODO Vérifier les champs séparement
                if (((etMail.getText().toString()).equals(etConfirmMail.getText().toString())) &&
                        ((etPassword.getText().toString()).equals(etConfirmPassword.getText().toString()))) {
                    //Vérifie le code d'accès
                    //TODO générer un code à l'aide du service web à la demande de l'adminitrateur PLUS TARD BEAUCOUP PLUS TARD
                    if (etAcessCode.getText().toString().equals("1234")) {
                        User user = new User(jsonObject);
                        //poste le user dans la base de données
                        WebServiceUserClient.getInstance().POSTUser(user);
                        Log.d("Ok", "Compte créé avec succès");
                        Toast.makeText(this, "Compte créé avec succès.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Log.d("Erreur", "Code incorrect");
                        Toast.makeText(this, "Code d'accès incorrect", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("Erreur", "Password & Mail");
                    Toast.makeText(this, "Erreur : Le mot de passe ou l'adresse mail doit être identiques", Toast.LENGTH_SHORT).show();
                }}
        else {
            Log.d("Ok", "Champs invalides");
            Toast.makeText(this, "Erreur : Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUsersReceived(ArrayList<User> users) {

    }

    @Override
    public void onUsersFailed(String error) {

    }
}

