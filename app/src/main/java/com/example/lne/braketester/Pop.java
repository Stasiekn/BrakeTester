package com.example.lne.braketester;


import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static com.example.lne.braketester.MainView.dsr;

public class Pop extends AppCompatActivity{

    ImageButton csv,pdf,email;
    double [] pomiaryacceleration1 = new double [5000];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.4));


                csv = (ImageButton) findViewById(R.id.csv);
                pdf = (ImageButton) findViewById(R.id.pdf);
                email = (ImageButton) findViewById(R.id.email);


                csv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getBaseContext(), "Zapisano plik CSV ", Toast.LENGTH_LONG).show();

                        int pol = MainView.dsr;
                        pomiaryacceleration1 = MainView.pomiaryacceleration;

                        File file = new File(MainView.path + "/Pomiary.csv");

                        PrintWriter printWriter = null;
                        try
                        {
                            printWriter = new PrintWriter(file);
                            for (int i=0; i<pol; i++)
                            {
                                printWriter.println( pomiaryacceleration1[i]-9.81);
                            }
                        }
                        catch (FileNotFoundException e){
                            e.printStackTrace();
                        }

                        printWriter.flush();
                        printWriter.close();
                    }

                });

                pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                     public void onClick(View v) {
                        Toast.makeText(getBaseContext(), "Zapisano plik PDF ", Toast.LENGTH_LONG).show();
                    }

                 });

                email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.i("Send email", "");
                        String[] TO = {""};
                        String[] CC = {""};
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);

                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                        emailIntent.putExtra(Intent.EXTRA_CC, CC);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Tytuł e-maila");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Treśc wiadomości");


                        try {
                            startActivity(Intent.createChooser(emailIntent, "Wyslij przez..."));
                            finish();
                            Log.i("Zakończono wysyłanie...", "");
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getBaseContext(), "Klient poczty nie zainstalowany", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}
