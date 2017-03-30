package com.imerir.bouillon.areapp.Activities;

import android.graphics.Color;
import android.opengl.EGLSurface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import com.imerir.bouillon.areapp.R;

/**
 * Created by maxime on 07/03/2017.
 */

public class RegisterStudentActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etfName;
    private EditText etmail;
    private EditText etConfirmMail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etPhoneNumber;
    private RadioButton cbCDPIR;
    private RadioButton cbCDSM;
    private RadioButton cbUPVD;
    private Button bRegister;

    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Inscription étudiant");
        setContentView(R.layout.activity_register_student);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(mToolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);

        mToolbar.inflateMenu(R.menu.main_menu);
        mToolbar.setTitle("Inscription Etudiant Imerir");
        mToolbar.setTitleTextColor(Color.WHITE);

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_backspace_white_24dp));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etName = (EditText) findViewById(R.id.etNameR);
        etfName = (EditText) findViewById(R.id.etfNameR);
        etmail = (EditText) findViewById(R.id.etMailLoginR);
        etConfirmMail = (EditText) findViewById(R.id.etConfirmMailR);
        etPassword = (EditText) findViewById(R.id.etPasswordR);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPasswordR);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumberR);
        cbCDPIR = (RadioButton) findViewById(R.id.cbCDPIR);
        cbCDSM = (RadioButton) findViewById(R.id.cbCDSM);
        cbUPVD = (RadioButton) findViewById(R.id.cbUPVD);
        bRegister = (Button) findViewById(R.id.bRegisterR);

    }
}
