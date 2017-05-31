package com.imerir.bouillon.areapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.imerir.bouillon.areapp.Fragments.DocumentListFragment;
import com.imerir.bouillon.areapp.Fragments.OffersListFragment;
import com.imerir.bouillon.areapp.Fragments.ProfilStudentFragment;
import com.imerir.bouillon.areapp.R;

import com.imerir.bouillon.areapp.Utils.GoogleApiSingleton;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

/**
 * @author Bouillon Maxime
 * @version 0.9
 * Classe gérant l'activité principale celle ci navique grace a une bottom bar a travers 3 ecrans différents déclarés dans des fragments
 */
public class MainActivity extends AppCompatActivity {

    GoogleApiClient googleApiClient;
    GoogleSignInOptions signInOptions;

    private Toolbar mToolbar;

    /**
     * @param savedInstanceState
     * Actions lancées a la création de l'activité principalement la mise en place de l'IHM
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().setHostedDomain("imerir.com").build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions)
                .build();



        //Si l'utilisateur n'a pas effectué de connexion on l'envoie vers l'activité connexion.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!preferences.getBoolean("isConnected", false)) {
            Intent loginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(loginActivityIntent);
        } else {
            setContentView(R.layout.activity_main);

            //Toolbar
            mToolbar = (Toolbar) findViewById(R.id.customToolbar);
            setSupportActionBar(mToolbar);

            final ActionBar ab = getSupportActionBar();
            ab.setDisplayShowTitleEnabled(false);

            mToolbar.inflateMenu(R.menu.main_menu);
            mToolbar.setTitle(getResources().getText(R.string.title_offer));
            mToolbar.setTitleTextColor(Color.WHITE);

            /**
             * Gestion des actions de la bottom bar, appel des fragments en fonction de l'onglet selectionné
             */
            //Initialisation de la vue principale + Mise en place des éléments du menu
            BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {
                    if (tabId == R.id.tab_offers) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentOffers, new OffersListFragment())
                                .commit();
                        mToolbar.setTitle(getResources().getText(R.string.title_offer));
                    }
                    if (tabId == R.id.tab_documents) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentOffers, new DocumentListFragment())
                                .commit();
                        mToolbar.setTitle(getResources().getText(R.string.title_document));
                    }
                    if (tabId == R.id.tab_profile) {

                        //PROFIL ETUDIANT
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentOffers, new ProfilStudentFragment())
                                .commit();
                        mToolbar.setTitle(getResources().getText(R.string.tab_profil_student));
                    }
                }
            });
        }
    }

    /**
     * Methode deconnexion permettant de se deconnecter de l'application
     * + Deconnexion de Google
     */
    private void deconnexion() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putBoolean("isConnected", false).commit();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginActivityIntent);
                        finish();
                    }
                });
    }

    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Deconnexion:
                deconnexion();
                return true;
            case R.id.Quitter:
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}