package com.example.lne.braketester;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Kalkulator extends AppCompatActivity {
    Button btnCofnij, btnWynik;
    static TextView wczasreakcji, wdrogareakcji, wdrogaham, wdrogazatrz, wczasham;


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
        btnWynik = (Button)findViewById(R.id.btnWynik);

        final EditText txtopoznienie = (EditText)findViewById(R.id.editImie);
        final EditText txtpredkosc = (EditText)findViewById(R.id.editpredkosc);
        final EditText txtintens = (EditText)findViewById(R.id.editIntens);
        final EditText txtreakcjakier = (EditText)findViewById(R.id.editczaskier);
        final EditText txtreakcjahamulcow = (EditText)findViewById(R.id.editczasham);






        wczasreakcji = (TextView)findViewById(R.id.wynikczasreakcji);
        wdrogareakcji = (TextView)findViewById(R.id.wynikdrogareakcji);
        wdrogaham = (TextView)findViewById(R.id.wynikdrogahamowania);
        wdrogazatrz = (TextView)findViewById(R.id.wynikdrogazatrz);
        wczasham = (TextView)findViewById(R.id.wynikczasham);


        btnCofnij.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        }
        );
        btnWynik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                double czasreakcjikier = Double.parseDouble(txtreakcjakier.getText().toString());
                double czasreakcjiham = Double.parseDouble(txtreakcjahamulcow.getText().toString());
                double intensham = Double.parseDouble(txtintens.getText().toString());
                double predkosc = Double.parseDouble(txtpredkosc.getText().toString());
                double opozn = Double.parseDouble(txtopoznienie.getText().toString());

                double intens1=intensham/100;
                double predkosc1 = predkosc/3.6;
                double czasreakcji = czasreakcjiham+czasreakcjikier;
                double drogareakcji=predkosc1*(czasreakcjikier+(czasreakcjiham/2));
                double drogaham=(predkosc1*predkosc1)/(2*opozn*intens1);
                double drogazatrz=drogaham+drogareakcji;
                double czasham=Math.sqrt((2*drogaham)/(opozn*intens1))+czasreakcji;




                wczasreakcji.setText ( new DecimalFormat("#.##").format(czasreakcji) + " s");
                wdrogareakcji.setText ( new DecimalFormat("#.##").format(drogareakcji) + " m");
                wdrogaham.setText ( new DecimalFormat("#.##").format(drogaham) + " m");
                wdrogazatrz.setText ( new DecimalFormat("#.##").format(drogazatrz) + " m");
                wczasham.setText ( new DecimalFormat("#.##").format(czasham) + " s");


            }
        });
    }
}
