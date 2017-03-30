package com.imerir.bouillon.areapp.Activities;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
        ab.setDisplayShowTitleEnabled(false);

        mToolbar.inflateMenu(R.menu.main_menu);
        mToolbar.setTitle("Inscription Responsable ARE");
        mToolbar.setTitleTextColor(Color.WHITE);

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_backspace_white_24dp));
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

    }


    @Override
    public void onClick(View view) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Nom",etName.getText().toString());
            jsonObject.put("Prenom",etfName.getText().toString());
            jsonObject.put("Mail",etMail.getText().toString());
            jsonObject.put("Password",etPassword.getText().toString());
            jsonObject.put("Telephone",etPhoneNumber.getText().toString());
            jsonObject.put("Type",1);
            jsonObject.put("Formation", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Todo verify mail et pw


        //TODO Test this
        User user = new User(jsonObject);
        if(etAcessCode.getText().toString().equals("1234"))
            WebServiceUserClient.getInstance().POSTUser(user);
        else
            Toast.makeText(this, "Code d'accès incorrect", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUsersReceived(ArrayList<User> users) {

    }

    @Override
    public void onUsersFailed(String error) {

    }
}

