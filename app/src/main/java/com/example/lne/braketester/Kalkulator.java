package com.example.lne.braketester;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Kalkulator extends AppCompatActivity {
    Button btnCofnij;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalkulator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setLogo(R.mipmap.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        btnCofnij = (Button)findViewById(R.id.btnCofnij);


        btnCofnij.setOnClickListener(new View.OnClickListener(){
                                         @Override
                                         public void onClick(View v) {
                                             finish();

                                         }
                                     }
        );
    }
}
