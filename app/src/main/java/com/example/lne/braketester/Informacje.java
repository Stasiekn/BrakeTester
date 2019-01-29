package com.example.lne.braketester;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;


public class Informacje extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    Switch list_toggle;

    Button btnCofnij, ButtonSendFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacje);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setLogo(R.mipmap.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Spinner spinnerRodzOpon = (Spinner) findViewById(R.id.spinnerRodzOpon);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.rodzajOpon, R.layout.spinner_item1);

        adapter.setDropDownViewResource(R.layout.spinner_item1);
        spinnerRodzOpon.setAdapter(adapter);

        btnCofnij = (Button)findViewById(R.id.btnCofnij);
        ButtonSendFeedback = (Button)findViewById(R.id.ButtonSendFeedback);

          btnCofnij.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(getApplicationContext(),MainView.class));
            }
        }
        );
          ButtonSendFeedback.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  EditText txtname = (EditText)findViewById(R.id.editImie);
                  String name    =  txtname.getText().toString();

                  EditText txtmarka = (EditText)findViewById(R.id.editMarka);
                  String marka    =  txtmarka.getText().toString();

                  EditText txtmarkaOpon = (EditText)findViewById(R.id.editMarkaOpon);
                  String markaOpon    =  txtmarkaOpon.getText().toString();

                  EditText txtCisnienie = (EditText)findViewById(R.id.editCisnienie);
                  String cisnienie    =  txtCisnienie.getText().toString();

                  EditText txtMiejsce = (EditText)findViewById(R.id.editMiejsce);
                  String miejsce    =  txtMiejsce.getText().toString();

                   Spinner feedbackSpinner = (Spinner) findViewById(R.id.spinnerRodzOpon);
                  String rodzOpon = feedbackSpinner.getSelectedItem().toString();


              }
          });

        list_toggle=(Switch)findViewById(R.id.switch1);
        list_toggle.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            list_toggle.setText("Włączony   ");  //To change the text near to switch
            Log.d("You are :", "Checked");
            Toast.makeText(getBaseContext(), "ABS WŁĄCZONY ", Toast.LENGTH_SHORT).show();
        }
        else {
            list_toggle.setText("Wyłączony   ");   //To change the text near to switch
            Log.d("You are :", " Not Checked");
            Toast.makeText(getBaseContext(), "ABS WYŁĄCZONY ", Toast.LENGTH_SHORT).show();
        }

    }
}
