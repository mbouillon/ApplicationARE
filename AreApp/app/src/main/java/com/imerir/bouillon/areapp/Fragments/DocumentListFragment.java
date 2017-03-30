package com.imerir.bouillon.areapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imerir.bouillon.areapp.Adapters.DocumentAdapter;
import com.imerir.bouillon.areapp.Clients.WebServiceDocumentClient;
import com.imerir.bouillon.areapp.Models.Document;
import com.imerir.bouillon.areapp.R;

import java.util.ArrayList;

/**
 * Created by maxime on 09/03/2017.
 */

public class DocumentListFragment extends Fragment implements View.OnClickListener, WebServiceDocumentClient.OnDocumentsListListener, DocumentAdapter.OnDocumentClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_document, null);
    }

    RecyclerView recyclerView;

    ArrayList<Document> _document;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _document = new ArrayList<Document>();

        //Assisgnation du recycler view + Document Adapter + WebService
        WebServiceDocumentClient.getInstance().requestDocuments(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.documentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        WebServiceDocumentClient.getInstance().getDocuments();

    }







    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDocumentClicked(Document document) {

    }

    @Override
    public void onDocumentsReceived(ArrayList<Document> Documents) {
        recyclerView.setAdapter(new DocumentAdapter(Documents,this));
    }

    @Override
    public void onDocumentsFailed(String error) {

    }
}
