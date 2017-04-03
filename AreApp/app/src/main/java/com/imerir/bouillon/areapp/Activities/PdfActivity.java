package com.imerir.bouillon.areapp.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.imerir.bouillon.areapp.Clients.WebServiceDocumentClient;
import com.imerir.bouillon.areapp.Models.Document;
import com.imerir.bouillon.areapp.R;

/**
 * Created by SireRemy on 31/03/2017.
 */
public class PdfActivity extends AppCompatActivity {

    //Permission de lire et ecrire
    private static final int ENABLE_READ_EXTERNAL_STORAGE_REQUEST_ID = 3;
    private static final int ENABLE_WRITE_EXTERNAL_STORAGE_REQUEST_ID = 4;

    Document document;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        document = WebServiceDocumentClient.getInstance().getDocument(getIntent().getIntExtra("document_id", 0));

        //Toolbar + action flêche retour
        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(document.getDocumentURL());
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
            // Appelez quelques API de conception de matériaux ici
        } else {
            // Implémentez cette fonctionnalité sans la conception du matériau
        }

        //Ouverture dans le webview
        WebView webview = (WebView) findViewById(R.id.activity_main_webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 4.1.2; C1905 Build/15.1.C.2.8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.114 Mobile Safari/537.36");

        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        PdfWebViewClient pdfWebViewClient = new PdfWebViewClient(this, webview);
        pdfWebViewClient.loadPdfUrl(
                "http://docs.google.com/gview?embedded=true&url=http://mod.ctpmperpignan.com/pdf/pdf_lignes/Ligne15.pdf");
        //TODO lier a notre webWervice
        // "http://docs.google.com/viewer?url=" + document.getDocId(Integer.parseInt((String) view.getTag())).pdfURL), "text/html");
        //Uri.parse("http://10.0.2.2:5000/DocUps/" + document.getDocId()));
    }

    private class PdfWebViewClient extends WebViewClient
    {
        private static final String TAG = "PdfWebViewClient";
        private static final String PDF_EXTENSION = ".pdf";
        private static final String PDF_VIEWER_URL = "http://docs.google.com/gview?embedded=true&url=";

        private Context mContext;
        private WebView mWebView;
        private ProgressDialog mProgressDialog;
        private boolean isLoadingPdfUrl;

        public PdfWebViewClient(Context context, WebView webView)
        {
            mContext = context;
            mWebView = webView;
            mWebView.setWebViewClient(this);
        }

        public void loadPdfUrl(String url)
        {
            mWebView.stopLoading();

            if (!TextUtils.isEmpty(url))
            {
                isLoadingPdfUrl = isPdfUrl(url);
                if (isLoadingPdfUrl)
                {
                    mWebView.clearHistory();
                }

                showProgressDialog();
            }

            mWebView.loadUrl(url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url)
        {
            return shouldOverrideUrlLoading(url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl)
        {
            handleError(errorCode, description.toString(), failingUrl);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request)
        {
            final Uri uri = request.getUrl();
            return shouldOverrideUrlLoading(webView, uri.toString());
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public void onReceivedError(final WebView webView, final WebResourceRequest request, final WebResourceError error)
        {
            final Uri uri = request.getUrl();
            handleError(error.getErrorCode(), error.getDescription().toString(), uri.toString());
        }

        @Override
        public void onPageFinished(final WebView view, final String url)
        {
            Log.i(TAG, "Finished loading. URL : " + url);
            dismissProgressDialog();
        }

        private boolean shouldOverrideUrlLoading(final String url)
        {
            Log.i(TAG, "shouldOverrideUrlLoading() URL : " + url);

            if (!isLoadingPdfUrl && isPdfUrl(url))
            {
                mWebView.stopLoading();

                final String pdfUrl = PDF_VIEWER_URL + url;

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        loadPdfUrl(pdfUrl);
                    }
                }, 300);

                return true;
            }

            return false; // Charger url dans le webView lui-même
        }

        private void handleError(final int errorCode, final String description, final String failingUrl)
        {
            Log.e(TAG, "Error : " + errorCode + ", " + description + " URL : " + failingUrl);
        }

        private void showProgressDialog()
        {
            dismissProgressDialog();
            mProgressDialog = ProgressDialog.show(mContext, "", "Loading...");
        }

        private void dismissProgressDialog()
        {
            if (mProgressDialog != null && mProgressDialog.isShowing())
            {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }

        private boolean isPdfUrl(String url)
        {
            if (!TextUtils.isEmpty(url))
            {
                url = url.trim();
                int lastIndex = url.toLowerCase().lastIndexOf(PDF_EXTENSION);
                if (lastIndex != -1)
                {
                    return url.substring(lastIndex).equalsIgnoreCase(PDF_EXTENSION);
                }
            }
            return false;
        }
    }

    //Assosie le menu au menu_ducument_pdf
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_document_pdf, menu);
        return true;
    }

    //Assigne chaque item du menu a sont action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.download:
                // TODO download le pdf en cours
                //Version web
                Toast.makeText(getApplicationContext(), "Download...", Toast.LENGTH_LONG).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mod.ctpmperpignan.com/pdf/pdf_lignes/Ligne15.pdf"));
                //Uri.parse("http://10.0.2.2:5000/DocUps/" + document.getDocId()));
                startActivity(browserIntent);
                return true;
            case R.id.deleteDocument:
                // TODO Pouvoir supprimer le pdf en cours
                //deleteDocument();
                //Toast.makeText(getApplicationContext(), "Delete...", Toast.LENGTH_LONG).show();
                //super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
