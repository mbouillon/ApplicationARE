package com.imerir.bouillon.areapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.imerir.bouillon.areapp.Clients.WebServiceUserClient;
import com.imerir.bouillon.areapp.Models.User;
import com.imerir.bouillon.areapp.R;

import java.util.ArrayList;

/**
 * Created by maxime on 07/03/2017.
 */

public class LoginActivity extends AppCompatActivity implements WebServiceUserClient.OnUsersListListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 647;
    GoogleApiClient mGoogleApiClient;
    SignInButton signInButton;

    ArrayList<User> _user;

    User user;

    TextView mail;

    //Toolbar
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        WebServiceUserClient.createInstance(this);

        WebServiceUserClient.getInstance().requestUsers(this);
        _user = WebServiceUserClient.getInstance().getUsers();


        ////GOOGLE SIGN-IN////
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().setHostedDomain("imerir.com").build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        /////////////////////////


        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getText(R.string.app_name));
        mToolbar.setTitleTextColor(Color.WHITE);



        mail = (TextView) findViewById(R.id.etMailLoginR);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    private void handleResult(GoogleSignInResult result)
    {
        User user;
        boolean cr = false;

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            mail.setText(acct.getDisplayName());

            /*
                Si l'utilisateur est enregistré dans la base de données on continue sur la main activity
                Sinon il est redirigé vers les ecrans d'inscription pour indiquer leurs informations
            */

            for (int i = 0; i< _user.size(); i++){
                if(_user.get(i).getMail().equals(acct.getEmail())){
                    cr = true;
                }
            }

            if(!cr){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.account_not_registered);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.bt_login_register_responsable),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Intent intent = new Intent(getApplicationContext(), RegisterResponsableActivity.class);
                                intent.putExtra("mail", acct.getEmail());
                                intent.putExtra("famName", acct.getFamilyName());
                                intent.putExtra("name", acct.getGivenName());
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(getString(R.string.bt_login_register_student),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Intent intent = new Intent(getApplicationContext(), RegisterStudentActivity.class);
                                intent.putExtra("mail", acct.getEmail());
                                intent.putExtra("famName", acct.getFamilyName());
                                intent.putExtra("name", acct.getGivenName());
                                startActivity(intent);

                            }
                        })
                        .setNeutralButton(getString(R.string.ms_callDialog_stop),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
            else{
                user = WebServiceUserClient.getInstance().getUser(acct.getEmail());
                preferences.edit().putBoolean("isConnected", true).apply();
                preferences.edit().putString("mail", acct.getEmail()).apply();
                preferences.edit().putBoolean("type", user.getType()).apply();
                preferences.edit().putInt("id", user.getId()).apply();
                preferences.edit().putInt("formation", user.getFormation()).apply();
                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivityIntent);
                finish();
            }
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onUsersReceived(ArrayList<User> users) {
        _user = users;
    }

    @Override
    public void onUsersFailed(String error) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
