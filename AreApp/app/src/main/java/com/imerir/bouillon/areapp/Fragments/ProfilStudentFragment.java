package com.imerir.bouillon.areapp.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.imerir.bouillon.areapp.Clients.WebServiceUserClient;
import com.imerir.bouillon.areapp.Models.User;
import com.imerir.bouillon.areapp.R;

import java.util.ArrayList;

import static com.imerir.bouillon.areapp.Utils.App.addChar;

/**
 * @author Sire Rémy
 * @version 0.9
 * Fragment appellé dans la main activity gérant le profil étudiant
 */
public class ProfilStudentFragment extends Fragment implements View.OnClickListener, WebServiceUserClient.OnUsersListListener {

    private static final int PICK_FILE_REQUEST_CV = 1;
    private static final String TAG = ProfilStudentFragment.class.getSimpleName();
    private String selectedFilePath;

    ProgressDialog dialog;

    //Gestion de la Progress Bar des données
    ProgressDialog loadingDialog;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    int internet;

    //Swipe load
    private SwipeRefreshLayout mSwipeRefreshLayoutEtudiant;

    User user;

    TextView name, formation, cv, lm, etiquette, dashboard, telephone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profil_student, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        name = (TextView) view.findViewById(R.id.firstname);
        formation = (TextView) view.findViewById(R.id.formation);
        cv = (TextView) view.findViewById(R.id.cv);
        lm = (TextView) view.findViewById(R.id.lm) ;
        etiquette = (TextView) view.findViewById(R.id.etiquette);
        dashboard = (TextView) view.findViewById(R.id.dashboard);
        telephone = (TextView) view.findViewById(R.id.telephone);

        user = WebServiceUserClient.getInstance().getUserById(preferences.getInt("id", 0));

        name.setText(user.getPrenom().toString() + " " + user.getNom().toString().toUpperCase());
        String tel;
        tel = addChar(user.getTelephone().toString(), " ", 2);
        telephone.setText(tel);
        switch (user.getFormation()) {
            case 1:
                formation.setText("CDPIR");
                break;
            case 2:
                formation.setText("CDSM");
                break;
            case 3:
                formation.setText("UPVD");
                break;
            default:
                formation.setText("CDPIR");
        }

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertNotImplemented();
            }
        });

        lm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertNotImplemented();
            }
        });

        etiquette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertNotImplemented();
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertNotImplemented();
            }
        });
    }



    private void showFileChooser() {
        Intent intent = new Intent();
        //Définit le fichier de sélection sur tous les types de fichiers
        intent.setType("*/*");
        //Permet de sélectionner les données et de les retourner
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Démarre une nouvelle activité pour sélectionner les fichiers et retourner les données
        startActivityForResult(Intent.createChooser(intent, getString(R.string.ms_select_file)),PICK_FILE_REQUEST_CV);
    }

    @Override
    public void onClick(View view) {
        if(view == cv){
            showAlertNotImplemented();
        }
        if(view == lm){
            showAlertNotImplemented();
        }
        if(view == etiquette){
            showAlertNotImplemented();
        }
        if(view == dashboard){
            showAlertNotImplemented();
        }
    }

    @Override
    public void onUsersReceived(ArrayList<User> users) {

    }

    @Override
    public void onUsersFailed(String error) {

    }

    private void showAlertNotImplemented(){
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.ms_warning))
                .setMessage(getString(R.string.alert))
                .setPositiveButton(getString(R.string.ms_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
