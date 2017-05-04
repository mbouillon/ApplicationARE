package com.imerir.bouillon.areapp.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.github.clans.fab.FloatingActionMenu;
import com.imerir.bouillon.areapp.Clients.WebServiceUserClient;
import com.imerir.bouillon.areapp.Models.User;
import com.imerir.bouillon.areapp.R;

import java.util.ArrayList;

import static com.imerir.bouillon.areapp.Utils.App.md5;

/**
 * Created by maxime on 07/03/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, WebServiceUserClient.OnUsersListListener {

    private ArrayList<User> _user;
    User user;

    com.github.clans.fab.FloatingActionMenu floatingActionMenu;
    com.github.clans.fab.FloatingActionButton fbaStudent, fbaResponsable;
    EditText mail, password;
    TextView tvAstuce;
    Button connexion;

    //Gestion de la Progress Bar
    ProgressDialog loadingDialog;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private Response.Listener listener;
    int internet;

    //Gestion du Remenber Me Email
    CheckBox cbRemember;
    private String email;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    //Toolbar
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Connexion");

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("ARE - IMERIR");
        mToolbar.setTitleTextColor(Color.WHITE);

        //Si internet est activé alors on affiche un temps de chargement pour dl les données
        if (checkConnectivity() == 1){
            showLoadingDialogData();
        }

        WebServiceUserClient.createInstance(this);

        mail = (EditText) findViewById(R.id.etMailLoginR);
        password = (EditText) findViewById(R.id.etPasswordLogin);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);
        connexion = (Button) findViewById(R.id.bConnexionLogin);
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fbaMenu);
        fbaResponsable = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fbaResponsable);
        fbaStudent = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fbaEtudiant);

        WebServiceUserClient.getInstance().requestUsers(this);

        //Associe le bouton connexion au click "onClick"
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

        //Création des préférences à sauvegarder
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            mail.setText(loginPreferences.getString("email", ""));
            cbRemember.setChecked(true);
        }

        //TODO Delete after tests
        //mail.setText("maxime.bouillon@imerir.com");
        //password.setText("maxime");
    }

    @Override
    public void onClick(View v) {
        String _mail = mail.getText().toString();
        String _password = password.getText().toString();
        if (checkUser(_mail, _password)) {

            //Sauvegarde la dernière adresse mail
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mail.getWindowToken(), 0);
            email = mail.getText().toString();

            //Injecte la derniere adresse mail
            if (cbRemember.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("email", email);
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }

            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            preferences.edit().putBoolean("isConnected", true).commit();
            //TODO Implements this for connexion
            //Enregistrez l'utilisateur ... et le type à envoyer des requêtes au serveur avec cet identifiant
            //Et enregistrez le type d'utilisateur pour déterminer si l'application est utilisée par un responsable ou un étudiant
            preferences.edit().putString("mail", _mail).commit();
            preferences.edit().putString("password", _password).commit();
            preferences.edit().putBoolean("type", user.getType()).commit();
            preferences.edit().putInt("id", user.getId()).commit();
            preferences.edit().putInt("formation", user.getFormation()).commit();
            finish();

        } else  {
            //Vérifie la présence de réseau
            if (checkConnectivity() == 0){

            }else {
                Toast.makeText(this, "Erreur, identifiants incorrects", Toast.LENGTH_SHORT).show();
            }
        }
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
            //Vérifie la présence de réseau
            if (checkConnectivity() == 0){

            }else{
                Toast.makeText(this, "Erreur, Vous ne possedez pas de compte", Toast.LENGTH_SHORT).show();
            }
        }
        return response;
    }

    @Override
    public void onUsersReceived(ArrayList<User> users) {
        //Récuperation des USERS, puis on renvoi la boîte de dialogue de progression
        _user = users;
        loadingDialog.dismiss();
    }

    @Override
    public void onUsersFailed(String error) {

    }

    //Création du menu menu_login
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return true;
    }

    //Assigne chaque item du menu a sont action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.restartButton:
                //Au clique, il démarre l'activité et ferme celui dans lequel vous vous trouvez
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Gestion du Progress Dialog
    public void showLoadingDialogData() {
        try {
            //Création d'un ProgressDialog et l'afficher
            loadingDialog = ProgressDialog.show(this, "Veuillez patienter", "Chargement en cours...", true, false);
            //Création d'un Thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (progressStatus < 100) {
                        // Mise à jour l'état de progression
                        progressStatus += 1;
                        // Essayez de suspendre le Thread pendant x secondes
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // Mise à jour la barre de progression fictive
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // Mise à jour de l'état de progression
                                loadingDialog.setProgress(progressStatus);
                                // Si l'exécution de la tâche est terminée
                                if (progressStatus == 100) {
                                    //Renvoi la boîte de dialogue de progression
                                    loadingDialog.dismiss();
                                }
                            }
                        });
                    }
                }
            }).start(); // Démarrez l'opération
        } catch (Exception e) {

        }
    }

    //Verification de la connexion internet du téléphone
    private int checkConnectivity() {
        boolean enabled = true;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        //Vérification si le téléphone est connecté a un réseau mobile, wifi ou pas
        if ((info == null || !info.isConnected() || !info.isAvailable())) {
            //Aucune connexion internet
            internet = 0;
            Log.d("Internet", "OFF");
            Toast.makeText(getApplicationContext(), "Aucune connexion réseau.", Toast.LENGTH_SHORT).show();
            enabled = false;
        } else {
            //Le réseau est connecté
            Log.d("Internet", "ON");
            internet = 1;
        }
        return internet;
    }
}
