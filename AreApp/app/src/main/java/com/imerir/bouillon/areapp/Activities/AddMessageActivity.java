package com.imerir.bouillon.areapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.imerir.bouillon.areapp.Clients.WebServiceMessageClient;
import com.imerir.bouillon.areapp.Models.WelcomeMessage;
import com.imerir.bouillon.areapp.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * @author Bouillon Maxime
 * @version 0.9
 * Classe gérant l'activité permettant d'ajouter un message
 */

public class AddMessageActivity extends AppCompatActivity implements TextWatcher{


    private Toolbar mToolbar;
    private TextView nbCarRestants;
    private Button sendMessage;
    private EditText etMessage;


    /**
     * @param savedInstanceState
     *
     * Actions lancées a la création de l'activité principalement la mise en place de l'IHM
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);

        /**
         * Gestion de la toolbar
         */

        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        mToolbar.inflateMenu(R.menu.main_menu);
        mToolbar.setTitle(getResources().getText(R.string.title_add_message));
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /**
         * Cast des widgets
         */
        nbCarRestants = (TextView) findViewById(R.id.nbCarRestants);
        sendMessage = (Button) findViewById(R.id.btnPublierMessage);
        etMessage = (EditText) findViewById(R.id.etMessage);

        /**
         *Initialise la val par défaut de nbCarRestants
         */
        nbCarRestants.setText(getString(R.string.ms_characters_255));
        /**
         * Attache le listener TextWatcher sur l'EditText
         */
        etMessage.addTextChangedListener(this);

        /**
         * Au click du bouton
         */
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Construction du json pour le post avec les données dans le champs
                 */
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Message", etMessage.getText().toString());
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    jsonObject.put("PublisherId", preferences.getInt("id", 0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /**
                 * Gestion du cas champ vide si non rempli
                 */
                if(etMessage.getText().toString().isEmpty()) Toast.makeText(getBaseContext(), getString(R.string.ms_error_message), Toast.LENGTH_SHORT).show();
                else
                {
                    /**
                     * Construction de l'objet json qui va passer par une requete http vers le serveur
                     * + Appel de la methode pour passer le post
                     * + Redirection vers la mainActivity
                     * + Desctruction de l'activity
                     */
                    WelcomeMessage wm = new WelcomeMessage(jsonObject);
                    WebServiceMessageClient.getInstance().POSTMessage(wm, getString(R.string.url_message));
                    Toast.makeText(getApplicationContext(), getString(R.string.ms_success_message), Toast.LENGTH_LONG).show();
                    Intent MainActivityIntent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(MainActivityIntent);
                    finish();
                }
            }
        });

    }

    //

    /**
     *
     * @param editable
     * Gestion du nombre de caractères restants
     * Méthode liée au listenner elle vérifie le champ texte après chaque changement de texte
     */
    @Override
    public void afterTextChanged(Editable editable){
        int nbChar = etMessage.getText().toString().length();
        int leftChar = 255 - nbChar;
        nbCarRestants.setText(Integer.toString(leftChar) + " " + getString(R.string.ms_characters));
        if (leftChar > 30)
            nbCarRestants.setTextColor(Color.GRAY);
        if (leftChar < 30 && leftChar >= 0)
            nbCarRestants.setTextColor(Color.RED);
    }

    /**
     *
     * @param s
     * @param start
     * @param count
     * @param after
     *
     * Methodes obligatoires liées à l'implémentationn de TextWatcher
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

}
