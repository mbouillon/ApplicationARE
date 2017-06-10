package com.imerir.bouillon.areapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.imerir.bouillon.areapp.Activities.StudentDetailActivity;
import com.imerir.bouillon.areapp.Adapters.UsersListAdapter;
import com.imerir.bouillon.areapp.Clients.WebServiceUserClient;
import com.imerir.bouillon.areapp.Models.User;
import com.imerir.bouillon.areapp.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by SireRemy on 31/05/2017.
 */

public class ProfilResponsableFragment extends Fragment implements View.OnClickListener, WebServiceUserClient.OnUsersListListener, UsersListAdapter.OnUserClickListener  {

    ArrayList<User> _user;
    ArrayList<User> _userCopy3;
    ArrayList<User> _userCopy2;
    ArrayList<User> _userCopy1;
    ArrayList<User> _userCopy;
    RecyclerView studentsList;

    //Gestion de la Progress Bar des données
    ProgressDialog loadingDialog;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    int internet;

    //Swipe load
    private SwipeRefreshLayout mSwipeRefreshLayoutResponsable;

    //Bar de recherche
    SearchView searchView ;

    RadioGroup radioGroup;
    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profil_responsable, null);
    }

    /**
     * Quand la vue est créée faire...
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ///Si internet est activé alors on affiche un temps de chargement pour dl les données
        if (checkConnectivity() == 1){
            showLoadingDialog();
        }

        //Assisgnation du recycler view + Document Adapter + WebService a l'arrayList
        WebServiceUserClient.getInstance().requestUsers(this);
        _user = new ArrayList<>();
        _user = WebServiceUserClient.getInstance().getUsers();
        studentsList = (RecyclerView) view.findViewById(R.id.studentList);
        studentsList.setLayoutManager(new LinearLayoutManager(getContext()));
        studentsList.setAdapter(new UsersListAdapter(_user,this));

        _userCopy2 = new ArrayList<>();
        _userCopy1 = new ArrayList<>();
        _userCopy = new ArrayList<>();
        _userCopy3 = new ArrayList<>();

        //Bar de recherche
        searchView = (SearchView) view.findViewById(R.id.rechercheView);
        searchView.setQueryHint("Recherche");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //TODO SearchView : faire remonter l'item qui correspond au texte
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getActivity(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getActivity(), newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });

        //RadioGroup
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case(R.id.radioButtonAll):
                        for (int m =0; m<_user.size(); m++) {
                            _userCopy3.add(_user.get(m));
                        }
                        studentsList.setAdapter(new UsersListAdapter(_userCopy3));
                        break;
                    case R.id.radioButtonCDPIR:
                        for (int m =0; m<_user.size(); m++) {
                            _userCopy.add(_user.get(m));
                        }
                        for (int l = _user.size() - 1; l >= 0; l-- ){
                            User user = _userCopy.get(l);
                            if(user.getFormation() != 1){
                                _userCopy.remove(l);
                            }
                        }
                        studentsList.setAdapter(new UsersListAdapter(_userCopy));
                        break;
                    case R.id.radioButtonCDSM:
                        for (int m =0; m<_user.size(); m++) {
                            _userCopy1.add(_user.get(m));
                        }
                        for (int l = _user.size() - 1; l >= 0; l-- ){
                            User user = _userCopy1.get(l);
                            if(user.getFormation() != 2){
                                _userCopy1.remove(l);
                            }
                        }
                        studentsList.setAdapter(new UsersListAdapter(_userCopy1));
                        break;
                    case R.id.radioButtonUPVD:
                        for (int m =0; m<_user.size(); m++) {
                            _userCopy2.add(_user.get(m));
                        }
                        for (int l = _user.size() - 1; l >= 0; l-- ){
                            User user = _userCopy2.get(l);
                            if(user.getFormation() != 3){
                                _userCopy2.remove(l);
                            }
                        }
                        studentsList.setAdapter(new UsersListAdapter(_userCopy2));
                        break;
                    default:
                        studentsList.setAdapter(new UsersListAdapter(_user));
                }
            }
        });

        //Swipe
        mSwipeRefreshLayoutResponsable = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_reponsable);
        mSwipeRefreshLayoutResponsable.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupRefreshSwipe();
                        mSwipeRefreshLayoutResponsable.setRefreshing(false);
                    }
                }, 2500);
            }
        });
    }

    /**
     * Méthode qui récupère les nouvelles données s'il y en a
     */
    private void setupRefreshSwipe(){
        WebServiceUserClient.getInstance().requestUsers(this);
        WebServiceUserClient.getInstance().getUsers();
    }

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

    }


    /**
     *
     * @param users
     */
    @Override
    public void onUsersReceived(ArrayList<User> users) {
        //studentsList.setAdapter(new UsersListAdapter(users,this));
        //Récuperation des Profil, puis on renvoi la boîte de dialogue de progression
        _user = users;
        loadingDialog.dismiss();
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
     * @param user
     */
    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getActivity(), StudentDetailActivity.class);
        intent.putExtra("user_id", user.getId());
        startActivity(intent);
    }

    /**
     * Gestion du Progress Dialog
     */
    public void showLoadingDialog(){
        try{
            //Création d'un ProgressDialog et l'afficher
            loadingDialog = ProgressDialog.show(getActivity(), "", "Chargement en cours...", true, false);
            //Création d'un Thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (progressStatus < 100) {
                        // Mise à jour l'état de progression
                        progressStatus += 1;
                        // Essayez de suspendre le Thread pendant x secondes
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // Mise à jour la barre de progression fictive
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // Mise à jour de l'état de progression
                                loadingDialog.setProgress(progressStatus);
                                // Si l'exécution de la tâche est terminée
                                if (progressStatus == 100) {
                                    //Renvoi la boîte de dialogue de progression
                                    loadingDialog.dismiss();
                                }
                            }
                        });
                    }
                }
            }).start(); // Démarrez l'opération
        }catch (Exception e){

        }
    }

    /**
     *
     * @return
     */
    private int checkConnectivity() {
        boolean enabled = true;

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        //Vérification si le téléphone est connecté a un réseau mobile, wifi ou pas
        if ((info == null || !info.isConnected() || !info.isAvailable())) {
            //Aucune connexion internet
            internet = 0;
            Log.d("Internet", "OFF");
            Toast.makeText(getActivity(), "Aucune connexion réseau.", Toast.LENGTH_SHORT).show();
            enabled = false;
        } else {
            //Le réseau est connecté
            Log.d("Internet", "ON");
            internet = 1;
        }
        return internet;
    }
}
