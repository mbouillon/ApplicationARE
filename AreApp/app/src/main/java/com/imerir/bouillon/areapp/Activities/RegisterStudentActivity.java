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
 * Created by maxime on 07/03/2017.
 */

public class RegisterStudentActivity extends AppCompatActivity implements WebServiceUserClient.OnUsersListListener {

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
    private boolean userExists = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

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

                Bundle extras = getIntent().getExtras();
                ArrayList<User> users = (ArrayList<User>) extras.getSerializable("userArray");

                //Save la couleur par defaut de la couleur du texte d'un champ pour set la couleur par défaut dans la gestion des erreurs
                ColorStateList oldColors =  etName.getTextColors();
                etMail.setTextColor(oldColors);
                etConfirmMail.setTextColor(oldColors);
                etPassword.setTextColor(oldColors);
                etConfirmPassword.setTextColor(oldColors);

                //Test si le user existe si oui passe un bool a true pour la vérification à l'inscription
                for(int i = 0; i < users.size(); i++){
                    if(users.get(i).getMail().equals(etMail.getText().toString()))
                        userExists = true;
                }

                //Creation d'un objet en JSON pour le POST Serveur afin de construire le modèle USER
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Nom", etName.getText().toString());
                    jsonObject.put("Prenom", etfName.getText().toString());
                    jsonObject.put("Mail", etMail.getText().toString());
                    jsonObject.put("Password", etPassword.getText().toString());
                    jsonObject.put("Telephone", etPhoneNumber.getText().toString());
                    jsonObject.put("Type", true);
                    jsonObject.put("Formation", formation);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (etName.getText().toString().isEmpty() || etfName.getText().toString().isEmpty() || etPhoneNumber.getText().toString().isEmpty() || etMail.getText().toString().isEmpty() || etConfirmMail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty() || etConfirmPassword.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterStudentActivity.this, getResources().getText(R.string.error_empty_fields), Toast.LENGTH_LONG).show();
                }
                else if(!etMail.getText().toString().equals(etConfirmMail.getText().toString())){
                    Toast.makeText(RegisterStudentActivity.this, getResources().getText(R.string.error_mail_not_equal), Toast.LENGTH_LONG).show();
                    etMail.setTextColor(Color.RED);
                    etConfirmMail.setTextColor(Color.RED);
                }
                else if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
                    Toast.makeText(RegisterStudentActivity.this, getResources().getText(R.string.error_password_not_equal), Toast.LENGTH_LONG).show();
                    etPassword.setTextColor(Color.RED);
                    etConfirmPassword.setTextColor(Color.RED);
                }
                else if(!etMail.getText().toString().contains("@imerir.com")){
                    Toast.makeText(RegisterStudentActivity.this, getResources().getText(R.string.error_mail_not_imerir), Toast.LENGTH_LONG).show();
                    etMail.setTextColor(Color.RED);
                }
                else if(userExists == true){
                    etMail.setTextColor(Color.RED);
                    Toast.makeText(RegisterStudentActivity.this, getResources().getText(R.string.error_account_already_exist), Toast.LENGTH_LONG).show();
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

    @Override
    public void onUsersReceived(ArrayList<User> users) {

    }

    @Override
    public void onUsersFailed(String error) {

    }
}
