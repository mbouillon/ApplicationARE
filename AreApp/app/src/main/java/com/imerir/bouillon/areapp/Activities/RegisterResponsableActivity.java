package com.imerir.bouillon.areapp.Activities;

import android.content.Intent;
import android.content.res.ColorStateList;
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
/**
 * @author Bouillon Maxime
 * @version 0.9
 * Classe gérant l'activité proposant l'inscription d'un responsable
 */
public class RegisterResponsableActivity extends AppCompatActivity implements WebServiceUserClient.OnUsersListListener {

    private EditText etName;
    private EditText etfName;
    private EditText etPhoneNumber;
    private EditText etAcessCode;
    private Button bRegister;
    private Toolbar mToolbar;

    private boolean userExists = false;

    /**
     *
     * @param savedInstanceState
     * Actions lancées a la création de l'activité principalement la mise en place de l'IHM
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_responsable);


        final Bundle extras = getIntent().getExtras();


        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);

        final ActionBar ab = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        mToolbar.inflateMenu(R.menu.main_menu);
        mToolbar.setTitle(getResources().getText(R.string.title_register_reponsable));
        mToolbar.setTitleTextColor(Color.WHITE);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etName = (EditText) findViewById(R.id.etNameR);
        etfName = (EditText) findViewById(R.id.etfNameR);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumberR);
        etAcessCode = (EditText) findViewById(R.id.etAcessCodeR);
        bRegister = (Button) findViewById(R.id.bRegisterR);


        etName.setText(extras.getString("name"));
        etfName.setText(extras.getString("famName"));

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Save la couleur par defaut de la couleur du texte d'un champ pour set la couleur par défaut dans la gestion des erreurs à chaque clic
                ColorStateList oldColors = etName.getTextColors();
                etAcessCode.setTextColor(oldColors);

                //Creation de l'objet JSON qui va servir a construire notre objet User
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Mail", extras.getString("mail"));
                    jsonObject.put("Nom", etName.getText().toString());
                    jsonObject.put("Prenom", etfName.getText().toString());
                    jsonObject.put("Telephone", etPhoneNumber.getText().toString());
                    jsonObject.put("type", false);
                    jsonObject.put("Formation", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (etName.getText().toString().isEmpty() || etfName.getText().toString().isEmpty() || etPhoneNumber.getText().toString().isEmpty() || etAcessCode.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterResponsableActivity.this, getResources().getText(R.string.error_empty_fields), Toast.LENGTH_LONG).show();
                }

                else if(!etAcessCode.getText().toString().equals("1234")){
                    Toast.makeText(RegisterResponsableActivity.this, getResources().getText(R.string.error_false_access_code), Toast.LENGTH_LONG).show();
                    etAcessCode.setTextColor(Color.RED);
                }
                else{
                    User user = new User(jsonObject);
                    WebServiceUserClient.getInstance().POSTUser(user);
                    Toast.makeText(getApplication(), getResources().getText(R.string.account_created_successfuly), Toast.LENGTH_SHORT).show();
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
    //Obligatoire
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

