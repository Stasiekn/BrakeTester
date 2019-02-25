package com.example.lne.braketester;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class Pop extends AppCompatActivity {

    ImageButton csv,pdf,email;

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
                         Toast.makeText(getBaseContext(), "Wysłano e-mail ", Toast.LENGTH_LONG).show();
                    }

                });
    }
}
