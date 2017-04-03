package com.imerir.bouillon.areapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imerir.bouillon.areapp.Utils.FilePath;
import com.imerir.bouillon.areapp.R;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by SireRemy on 31/03/2017.
 */

public class UploadFileActivity extends AppCompatActivity implements View.OnClickListener {

    //Permissions de lire et ecrire
    private static final int ENABLE_READ_EXTERNAL_STORAGE_REQUEST_ID = 1;
    private static final int ENABLE_WRITE_EXTERNAL_STORAGE_REQUEST_ID = 2;

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = UploadFileActivity.class.getSimpleName();
    private String selectedFilePath;

    //URL du serveur URL 10.0.2.2 localServeur Emulateur
    private String SERVER_URL = "http://10.0.2.2:5000/mobile/Document/";
    //Declaration de activity_main.xml
    ImageView ivAttachment;
    Button bUpload;
    TextView tvFileName;
    ProgressDialog dialog;

    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload_file);

        //Toolbar + action flêche retour
        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ajouter un document");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Demande la permmision de lire les données
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    ENABLE_READ_EXTERNAL_STORAGE_REQUEST_ID);
        }
        //Demande la permmision d'ecrire les données
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ENABLE_WRITE_EXTERNAL_STORAGE_REQUEST_ID);
        }

        // Vérifiez que nous fonctionnons sous Android 5.0 ou version ultérieure
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
        } else {
            // Implement this feature without material design
        }

        //Assignation
        ivAttachment = (ImageView) findViewById(R.id.ivAttachment);
        bUpload = (Button) findViewById(R.id.b_upload);
        tvFileName = (TextView) findViewById(R.id.tv_file_name);
        ivAttachment.setOnClickListener(this);
        bUpload.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v== ivAttachment){

            //Sur l'icône d'attachement cliquez sur l'image
            showFileChooser();
        }
        if(v== bUpload){

            //Quand clic Sur le bouton de téléchargement "Upload"
            if(selectedFilePath != null){
                dialog = ProgressDialog.show(UploadFileActivity.this,"","Envoi du fichier...",true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //Créer un nouveau thread à gérer Http Operations
                        uploadFile(selectedFilePath);
                    }
                }).start();
            }else{
                Toast.makeText(UploadFileActivity.this,"Veuillez choisir un fichier d'abord",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        //Définit le fichier de sélection sur tous les types de fichiers
        intent.setType("*/*");
        //Permet de sélectionner les données et de les retourner
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Démarre une nouvelle activité pour sélectionner les fichiers et retourner les données
        startActivityForResult(Intent.createChooser(intent,"Choisissez le fichier à envoyer.."),PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //Aucune donnée présente
                    return;
                }


                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this,selectedFileUri);
                Log.i(TAG,"Chemin du fichier sélectionné:" + selectedFilePath);

                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    tvFileName.setText(selectedFilePath);
                }else{
                    Toast.makeText(this,"Impossible de télécharger le fichier sur le serveur",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Android télécharger le fichier sur le serveur
    public int uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvFileName.setText("Le fichier source n'existe pas: " + selectedFilePath);
                }
            });
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Autoriser les entrées
                connection.setDoOutput(true);//Autoriser les sorties
                connection.setUseCaches(false);//N'utilisez pas de copie en cache
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);

                //Création de nouveaux dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //Écriture d'octets sur le flux de sortie de données
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //Renvoie non. D'octets présents dans fileInputStream
                bytesAvailable = fileInputStream.available();
                //En sélectionnant la taille du tampon comme minimum d'octets disponibles ou 1 Mo
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //Définir le tampon en tant que tableau d'octets de taille de bufferSize
                buffer = new byte[bufferSize];

                //Lire les octets de FileInputStream(Du 0 indice de tampon à buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //Loop répète jusqu'à bytesRead = -1, c'est-à-dire, aucun octet est laissé à lire
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "La réponse du serveur est: " + serverResponseMessage + ": " + serverResponseCode);

                //Le code de réponse de 200 indique l'état du serveur OK
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvFileName.setText("Chargement du fichier terminé.\n\n Vous pouvez voir le fichier télécharger ici: \n\n" + "http://10.0.2.2:5000/DocUps/"+ fileName);
                        }
                    });
                }

                //Fermer les flux d'entrée et de sortie
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UploadFileActivity.this,"Fichier non trouvé",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(UploadFileActivity.this, "Erreur d'URL!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(UploadFileActivity.this, "Impossible de lire / écrire un fichier!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }

    }
}
