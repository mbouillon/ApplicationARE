package com.imerir.bouillon.areapp.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.imerir.bouillon.areapp.Adapters.MessagesAdapter;
import com.imerir.bouillon.areapp.Clients.WebServiceMessageClient;
import com.imerir.bouillon.areapp.Models.WelcomeMessage;
import com.imerir.bouillon.areapp.R;

import java.util.ArrayList;

public class MessagesListActivity extends AppCompatActivity implements WebServiceMessageClient.OnMessagesListListener {


    RecyclerView recyclerView;

    ArrayList<WelcomeMessage> _message;

    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);


        _message = new ArrayList<WelcomeMessage>();

        //Assisgnation du recycler view + WebService
        WebServiceMessageClient.getInstance().requestAllMessages(this);
        recyclerView = (RecyclerView) findViewById(R.id.messagesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        WebServiceMessageClient.getInstance().getMessages();

        //Toolbar + action flêche retour
        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Anciens Messages Publiés");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onMessagesReceived(ArrayList<WelcomeMessage> messages) {
        recyclerView.setAdapter(new MessagesAdapter(messages, this));
    }

    @Override
    public void onMessagesFailed(String error) {

    }
}
