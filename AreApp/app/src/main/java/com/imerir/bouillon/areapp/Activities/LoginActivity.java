package com.imerir.bouillon.areapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.github.clans.fab.FloatingActionMenu;
import com.imerir.bouillon.areapp.Clients.WebServiceUserClient;
import com.imerir.bouillon.areapp.Models.User;
import com.imerir.bouillon.areapp.R;

import java.util.ArrayList;

import static com.imerir.bouillon.areapp.App.md5;

/**
 * Created by maxime on 07/03/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, WebServiceUserClient.OnUsersListListener{

    private ArrayList<User> _user;
    User user;

    com.github.clans.fab.FloatingActionMenu floatingActionMenu;
    com.github.clans.fab.FloatingActionButton fbaStudent, fbaResponsable;
    EditText mail, password;
    Button connexion;

    private Response.Listener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Connexion");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        WebServiceUserClient.createInstance(this);

        mail = (EditText) findViewById(R.id.etMailLoginR);
        password = (EditText) findViewById(R.id.etPasswordLogin);
        connexion = (Button) findViewById(R.id.bConnexionLogin);
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fbaMenu);
        fbaResponsable = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fbaResponsable);
        fbaStudent = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fbaEtudiant);

        WebServiceUserClient.getInstance().requestUsers(this);

        //TODO Delete after tests
        mail.setText("maxime.bouillon@imerir.com");
        password.setText("maxime");

        connexion.setOnClickListener(this);


        //Quand le fab inscription etudiant est selectionné on lance le fragment.
        fbaStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivityIntent = new Intent(LoginActivity.this, RegisterStudentActivity.class);
                startActivity(loginActivityIntent);
            }
        });

        //Quand le fab incription responsable est selectionné on lance le fragment correspondant.
        fbaResponsable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivityIntent = new Intent(LoginActivity.this, RegisterResponsableActivity.class);
                startActivity(loginActivityIntent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        String _mail = mail.getText().toString();
        String _password = password.getText().toString();
        if(checkUser(_mail, _password)){
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            preferences.edit().putBoolean("isConnected", true).commit();
            //TODO Implements this for connexion
            //Save the user... and the type for send requests to the server with this identifiers
            //And save the type of user to determinate if the app is used by responsable or student
            preferences.edit().putString("mail", _mail).commit();
            preferences.edit().putString("password", _password).commit();
            preferences.edit().putBoolean("type", user.getType()).commit();
            finish();
        }
        else {Toast.makeText(this, "Erreur, identifiants incorrects", Toast.LENGTH_SHORT).show();}
    }

    private boolean checkUser(String mail, String password) {
        user = WebServiceUserClient.getInstance().getUser(mail);
        boolean response = false;
        try {
            String sMail = user.getMail();
            String sPassword = user.getPassword();
            String pass = md5(password);

            if (sMail.equals(mail)) {
                if (sPassword.equals(pass)) {
                    response = true;
                }
            } else
                response = false;

        } catch (Exception e) {
            Toast.makeText(this, "Erreur, Vous ne possedez pas de compte", Toast.LENGTH_SHORT).show();
        }
        return response ;
    }

    @Override
    public void onUsersReceived(ArrayList<User> users) {
        _user = users;
    }

    @Override
    public void onUsersFailed(String error) {

    }
}
