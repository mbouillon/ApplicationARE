package com.imerir.bouillon.areapp.Activities;

import android.graphics.Color;
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

import com.imerir.bouillon.areapp.R;

import org.w3c.dom.Text;

public class AddMessageActivity extends AppCompatActivity implements TextWatcher{


    private Toolbar mToolbar;

    private TextView nbCarRestants;
    private Button sendMessage;
    private EditText etMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        mToolbar.inflateMenu(R.menu.main_menu);
        mToolbar.setTitle("Ajouter un message d'accueil ");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });





        //Cast des widgets
        nbCarRestants = (TextView) findViewById(R.id.nbCarRestants);
        sendMessage = (Button) findViewById(R.id.btnPublierMessage);
        etMessage = (EditText) findViewById(R.id.etMessage);
        //Initialise la val par défaut de nbCarRestants
        nbCarRestants.setText("255 Caractères restants...");
        //Attache le listener TextWatcher sur l'EditText
        etMessage.addTextChangedListener(this);

    }

    //Gestion du nombre de caractères restants
    @Override
    public void afterTextChanged(Editable editable){
        int nbChar = etMessage.getText().toString().length();
        int leftChar = 255 - nbChar;
        nbCarRestants.setText(Integer.toString(leftChar) + " Caractères restants...");
        if (leftChar > 30)
            nbCarRestants.setTextColor(Color.GRAY);
        if (leftChar < 30 && leftChar >= 0)
            nbCarRestants.setTextColor(Color.RED);
    }

    //Methodes obligatoires liées à l'implémentationn de TextWatcher
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
