package com.imerir.bouillon.areapp.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imerir.bouillon.areapp.Activities.MessagesListActivity;
import com.imerir.bouillon.areapp.Clients.WebServiceUserClient;
import com.imerir.bouillon.areapp.Models.User;
import com.imerir.bouillon.areapp.Models.WelcomeMessage;
import com.imerir.bouillon.areapp.R;

import java.util.ArrayList;

/**
 * Created by maxime on 12/03/2017.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> implements  WebServiceUserClient.OnUsersListListener {

    private ArrayList<WelcomeMessage> message;
    CardView cardView;
    private OnMessageClickListener listener;
    User user;

    public MessagesAdapter(ArrayList<WelcomeMessage> messages, MessagesListActivity listener) {
        this.message = messages;
        //this.listener = listener;
        WebServiceUserClient.getInstance().requestUsers(this);
    }

    @Override
    public MessagesAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessagesAdapter.MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessagesAdapter.MessageViewHolder holder, int position) {
        holder.update(message.get(position));
    }

    @Override
    public int getItemCount() {
        //return message.size();
        int a ;
        if(message != null && !message.isEmpty()) { a = message.size(); }
        else { a = 0; }
        return a;
    }

    @Override
    public void onUsersReceived(ArrayList<User> users) {

    }

    @Override
    public void onUsersFailed(String error) {

    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView PublishName;
        TextView Message;

        WelcomeMessage message;

        public MessageViewHolder(View itemView) {
            super(itemView);
            cardView    = (CardView) itemView.findViewById(R.id.cardView);
            PublishName = (TextView) itemView.findViewById(R.id.PublishName);
            Message = (TextView) itemView.findViewById(R.id.Message);

        }

        public void update(final WelcomeMessage mMessage) {
            message = mMessage;

            user = WebServiceUserClient.getInstance().getUserById(message.getPublisherId());
            PublishName.setText("Publié par : "+ user.getNom() + " " + user.getPrenom());
            //TODO crash quand on affiche le texte du message
            //Message.setText( message.getMessage());
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    //listener.onMessageClicked(mMessage);
                }
            });
        }
    }

    public interface OnMessageClickListener {
        void onMessageClicked(WelcomeMessage message);
    }

}
