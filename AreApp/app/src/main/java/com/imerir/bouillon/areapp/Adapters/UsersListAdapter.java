package com.imerir.bouillon.areapp.Adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imerir.bouillon.areapp.Clients.WebServiceUserClient;
import com.imerir.bouillon.areapp.Models.User;
import com.imerir.bouillon.areapp.R;

import java.util.ArrayList;

/**
 * Created by SireRemy on 31/05/2017.
 */

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.UserViewHolder> implements WebServiceUserClient.OnUsersListListener {

    private ArrayList<User> users;
    CardView cardView;
    private UsersListAdapter.OnUserClickListener listener;

    /**
     *
     * @param users
     * @param listener
     */
    public UsersListAdapter(ArrayList<User> users, UsersListAdapter.OnUserClickListener listener) {
        this.users = users;
        this.listener = listener;
    }
    public UsersListAdapter(ArrayList<User> users) {
        this.users = users;
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new UserViewHolder(v);
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(UsersListAdapter.UserViewHolder holder, int position) {
        holder.update(users.get(position));
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        int a;
        if(users != null && !users.isEmpty()) {a = users.size();}
        else {a = 0;}
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
     * ViewHolder
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView nomStudent;
        TextView prenomStudent;
        TextView formationStudent;
        TextView validLM;
        TextView validCV;
        TextView checkOffer;

        User user;

        /**
         *
         * @param itemView
         */
        public UserViewHolder(View itemView) {
            super(itemView);
            cardView    = (CardView) itemView.findViewById(R.id.cardViewUser);
            nomStudent = (TextView) itemView.findViewById(R.id.NomUser);
            prenomStudent = (TextView) itemView.findViewById(R.id.PrenomUser);
            formationStudent = (TextView) itemView.findViewById(R.id.FormationUser);
            validLM = (TextView) itemView.findViewById(R.id.validLM);
            validCV = (TextView) itemView.findViewById(R.id.validCV);
            checkOffer = (TextView) itemView.findViewById(R.id.offersCheck);
        }

        /**
         *
         * @param mUser
         */
        public void update(final User mUser) {
            user = mUser;
            nomStudent.setText(user.getNom());
            prenomStudent.setText(user.getPrenom());
            if(user.getFormation() == 0){
                formationStudent.setText("Responsable ARE ");
            }
            if(user.getFormation() == 1){
                formationStudent.setText("Formation : CDPIR");
            }
            if(user.getFormation() == 2){
                formationStudent.setText("Formation : CDSM");
            }
            if(user.getFormation() == 3){
                formationStudent.setText("Formation : UPVD");
            }
            if(user.getLmID() == 0){
                validLM.setText("Aucune LM Enregistrée");
                validLM.setTextColor(Color.RED);
            }else {
                validLM.setText("LM Enregistré");
                validLM.setTextColor(Color.GREEN);
            }
            if(user.getCvID() == 0){
                validCV.setText("Aucun CV Enregistré");
                validCV.setTextColor(Color.RED);
            }else {
                validCV.setText("CV Enregistré");
                validLM.setTextColor(Color.GREEN);
            }
            checkOffer.setText(user.getParticipations() + " Offre validés");
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUserClicked(mUser);
                }
            });
        }
    }

    /**
     * onUserClicked
     */
    public interface OnUserClickListener {
        void onUserClicked(User user);
    }
}
