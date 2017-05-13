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

import com.imerir.bouillon.areapp.Fragments.DocumentListFragment;
import com.imerir.bouillon.areapp.Fragments.OffersListFragment;
import com.imerir.bouillon.areapp.Fragments.ProfilStudentFragment;
import com.imerir.bouillon.areapp.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                        mToolbar.setTitle("Profil Etudiant");
                        //user = WebServiceUserClient.getInstance().getUserById(user.getId());
                        //mToolbar.setTitle("Profil" + user.getNom() + " " + user.getPrenom());



                    }
                }
            });
        }
    }

    //Methode deconnexion permettant de se deconnecter de l'app et oublie le compte sauvegardé
    private void deconnexion() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putBoolean("isConnected", false).commit();
        Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginActivityIntent);
        finish();
    }

}