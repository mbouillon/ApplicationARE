package com.imerir.bouillon.areapp.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imerir.bouillon.areapp.Clients.WebServiceUserClient;
import com.imerir.bouillon.areapp.Models.Document;
import com.imerir.bouillon.areapp.Models.User;
import com.imerir.bouillon.areapp.R;

import java.util.ArrayList;

/**
 * Created by maxime on 09/03/2017.
 */

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> implements WebServiceUserClient.OnUsersListListener {

    private ArrayList<Document> documents;
    CardView cardView;
    private OnDocumentClickListener listener;
    User user;

    public DocumentAdapter(ArrayList<Document> documents, OnDocumentClickListener listener) {
        this.documents = documents;
        this.listener = listener;
        WebServiceUserClient.getInstance().requestUsers(this);
    }

    @Override
    public DocumentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_item, parent, false);
        return new DocumentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DocumentViewHolder holder, int position) {
        holder.update(documents.get(position));
    }

    @Override
    public int getItemCount() {
        //return documents.size();
        int a ;
        if(documents != null && !documents.isEmpty()) { a = documents.size(); }
        else { a = 0; }
        return a;
    }

    @Override
    public void onUsersReceived(ArrayList<User> users) {

    }

    @Override
    public void onUsersFailed(String error) {

    }

    public class DocumentViewHolder extends RecyclerView.ViewHolder {

        TextView PublisherId;
        TextView PublishDate;
        TextView DocumentURL;

        Document document;

        public DocumentViewHolder(View itemView) {
            super(itemView);
            cardView    = (CardView) itemView.findViewById(R.id.cardView);
            PublisherId = (TextView) itemView.findViewById(R.id.PublisherId);
            PublishDate = (TextView) itemView.findViewById(R.id.PublishDate);
            DocumentURL = (TextView) itemView.findViewById(R.id.DocumentURL);
        }

        public void update(final Document mDocument) {
            document = mDocument;

            user = WebServiceUserClient.getInstance().getUserById(document.getPublisherId());
            PublisherId.setText(user.getNom() + " " + user.getPrenom());
            PublishDate.setText(document.getPublishDate());
            DocumentURL.setText(document.getDocumentURL());
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    listener.onDocumentClicked(mDocument);
                }
            });
        }
    }

    public interface OnDocumentClickListener {
        void onDocumentClicked(Document document);
    }
}