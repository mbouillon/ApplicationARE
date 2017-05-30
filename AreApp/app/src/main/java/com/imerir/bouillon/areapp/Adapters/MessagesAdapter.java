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

/**
 * @author Bouillon Maxime
 * @version 0.9
 * Classe gérant l'adapteur des données de la liste des Messages présent sur le MessagesListActivity
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> implements  WebServiceUserClient.OnUsersListListener {

    private ArrayList<WelcomeMessage> message;
    CardView cardView;
    User user;
    private OnMessageClickListener listener;

    /**
     *
     * @param messages
     * @param listener
     */
    public MessagesAdapter(ArrayList<WelcomeMessage> messages, MessagesListActivity listener) {
        this.message = messages;
        this.listener = listener;
        WebServiceUserClient.getInstance().requestUsers(this);
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MessagesAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessagesAdapter.MessageViewHolder(v);
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MessagesAdapter.MessageViewHolder holder, int position) {
        holder.update(message.get(position));
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        //return message.size();
        int a ;
        if(message != null && !message.isEmpty()) { a = message.size(); }
        else { a = 0; }
        return a;
    }

    /**
     *
     * @param users
     */
    @Override
    public void onUsersReceived(ArrayList<User> users) {

    }

    /**
     *
     * @param error
     */
    @Override
    public void onUsersFailed(String error) {

    }

    /**
     *
     */
    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView PublishName;
        TextView Message;

        WelcomeMessage message;

        /**
         *
         * @param itemView
         */
        public MessageViewHolder(View itemView) {
            super(itemView);
            cardView    = (CardView) itemView.findViewById(R.id.cardView);
            PublishName = (TextView) itemView.findViewById(R.id.PublishName);
            Message = (TextView) itemView.findViewById(R.id.MessageAccueil);

        }

        /**
         *
         * @param mMessage
         */
        public void update(final WelcomeMessage mMessage) {
            message = mMessage;

            user = WebServiceUserClient.getInstance().getUserById(message.getPublisherId());
            PublishName.setText(user.getNom() + " " + user.getPrenom());
            //TODO crash quand on affiche le texte du message
            Message.setText( message.getMessage());
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onMessageLongClicked(mMessage);
                    return true;
                }
            });
        }
    }

    /**
     *
     */
    public interface OnMessageClickListener {
        void onMessageClicked(WelcomeMessage message);
        void onMessageLongClicked(WelcomeMessage message);
    }

}
