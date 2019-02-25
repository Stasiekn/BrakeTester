package com.example.lne.braketester;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//Gauge

import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;


public class MainView extends AppCompatActivity implements SensorEventListener {

    public static float predkosc, predkosc1;

    //Zmienne------------------------------------------------------------------------------------------

    LocationService myService;
    static boolean status;
    LocationManager locationManager;
    static TextView distance, time, speed, wskaznik1, mfddtext, czasham, sham;
    Button btnStart, btnStop, btnPause, button2, runMDFF;
    static long startTime, stopTime;
    static ProgressDialog progressDialog;
    static  int p=0;
    static double sredniaakce, xaxis, yaxis, zaxis;
    //akcelerometr
    private Sensor mySensor;
    private SensorManager SM;
    //wykres przyspieszenia
    private Handler mHandler = new Handler();
    private LineGraphSeries<DataPoint> series;
    private double lastpoint = 2;
    int dsr=0;
    double suma=0,mfdd;

    // definicja tablicy pomiarow
    double [] pomiaryacceleration = new double [5000];
    float [] pomiarypredkosc = new float[5000];

    int w=0;


    //zapis wartoci do pliku
    int i;
    boolean predkoscgood =false, pomiar = false,koniec = false;

    public  String pol = "pol";
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BrakeTester";
    //Metody----------------------------------------------------------------------------------------



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
       final PointerSpeedometer speedometer = (PointerSpeedometer) findViewById(R.id.pointerSpeedometer);
        speedometer.setMaxSpeed(200);
        speedometer.setTickNumber(5);
        speedometer.setWithTremble(false);
        Thread t = new Thread(){

            public  void run(){

                while (!isInterrupted()){

                    try{
                        Thread.sleep(70);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                speedometer.speedTo(predkosc);

                                if (predkosc >= 30 && !predkoscgood && sredniaakce >= 13.5){
                                    startTime = System.currentTimeMillis();
                                    Toast.makeText(getBaseContext(), "Przekroczono 30 km/h i 2,19m/s ", Toast.LENGTH_SHORT).show();
                                    runMDFF.setVisibility(View.VISIBLE);
                                    predkoscgood = true;
                                    pomiar = true;
                                    wskaznik1.setText ("Prędkość hamowania: " + new DecimalFormat("#.##").format(predkosc) + " km/hr");


                                }
                                else if (predkosc <30){
                                    predkoscgood = false;
                                    if (predkosc < 3 && w>5 && !koniec){
                                        pomiar=false;
                                        koniectest();

                                    }
                                }

                            }
                        });
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setLogo(R.mipmap.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //INNE----


        //Wykres---
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>(new DataPoint[]{});
        graph.addSeries(series);
        graph.getViewport().setMaxY(20);
        graph.getViewport().setMinY(-10);
        graph.getViewport().setYAxisBoundsManual(true);

        //Wykres END---

        //zapis do pliku----

       File dir = new File(path);
        dir.mkdir();


        // Create our Sensor Manager
        SM = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        // Accelerometer Sensor
        assert SM != null;
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Register sensor Listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_FASTEST);


        //Request permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED);
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },1000);
            }
        }

        //Init variable
        distance = (TextView)findViewById(R.id.distance);
        time = (TextView)findViewById(R.id.time);
        speed = (TextView)findViewById(R.id.speed);
        wskaznik1 = (TextView)findViewById(R.id.wskaznik1);
        mfddtext = (TextView)findViewById(R.id.mfdd);
        czasham = (TextView)findViewById(R.id.czasham);
        sham = (TextView)findViewById(R.id.sham);


        runMDFF = (Button)findViewById(R.id.runMDFF);
        btnPause = (Button)findViewById(R.id.btnPause);
        btnStart = (Button)findViewById(R.id.btnStart);
        btnStop = (Button)findViewById(R.id.btnStop);
        button2 = (Button)findViewById(R.id.button2);// przycisk do zapisu do pliku



        checkGPS();
        runMDFF.setVisibility(View.GONE);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        assert locationManager != null;
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))

            return;
        if(!status)
            bindService();

        progressDialog = new ProgressDialog(MainView.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uzyskuje położenie...");
        progressDialog.show();







        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koniectest();

               // pomiar = true;





            }
        });
     btnPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                if (btnPause.getText().toString().equalsIgnoreCase("WSTRZYMAJ")) {
                    btnPause.setText("Wznów");
                    p = 1;
                } else if (btnPause.getText().toString().equalsIgnoreCase("WZNÓW")) {
                    checkGPS();
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                        return;
                    btnPause.setText("Wstrzymaj");
                    p = 0;
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(status==true)
                    unbindService();
                btnStart.setVisibility(View.VISIBLE);
                btnPause.setText("Wstrzymaj");
                btnPause.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(),Informacje.class));

            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick (View v) {
                startActivity(new Intent(MainView.this,Pop.class));

                File file = new File(path + "/Pomiary.csv");

                PrintWriter printWriter = null;
                try
                {

                    printWriter = new PrintWriter(file);
                    for (int i=0; i<dsr; i++)
                    {



                        printWriter.println( pomiaryacceleration[i]-9.81);


                    }
                }
                catch (FileNotFoundException e){
                    e.printStackTrace();
                }

                printWriter.flush();
                printWriter.close();

            }

        });
    }

    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem item = menu.findItem(R.id.spinner);
//Menu rozwijalne
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,

                R.array.spinner_list_item_array, R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_item);

        spinner.setAdapter(adapter);
//Koniec menu rozwijalne
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Toast.makeText(getBaseContext(), "Wybrano test MDFF ", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(getBaseContext(), "Wybrano test systemu ABS ", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        Toast.makeText(getBaseContext(), "Wybrano test hamowania silnikiem ", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(getBaseContext(), "Wybrano test sprawności hamulców ", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return true;
    }

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            myService = binder.getService();
            status = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            status=false;
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (status == true)
            unbindService();
    }


    private void unbindService() {
        if (status == false)
            return;
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        unbindService(sc);
        status = false;
    }

    @Override
    public void onBackPressed() {
        if (status == false)
            super.onBackPressed();
        else
            moveTaskToBack(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1000:
            {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this,"GRANTED", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "DENIED", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }


    private void checkGPS() {
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        assert locationManager != null;
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            showGPSDisabledAlert();
        else{
            Toast.makeText(this, "GPS jest aktywny", Toast.LENGTH_SHORT).show();
        }
    }

    private void showGPSDisabledAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enable GPS to use application")
                .setCancelable(false)
                .setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void bindService(){
        if (status == true)
            return;
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        bindService(i, sc, BIND_AUTO_CREATE);
        status = true;
        //startTime = System.currentTimeMillis();

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        xaxis = event.values[0];
        yaxis = event.values[1];
        zaxis = event.values[2];





        sredniaakce = Math.sqrt(xaxis*xaxis+yaxis*yaxis+zaxis*zaxis);
        addpointchart(); //mozna przeniesc do nowego watku
        if (w<5000 && pomiar) {
            pomiaryacceleration[w]=sredniaakce;
            pomiarypredkosc[w]=predkosc;


            w = w + 1;


        }
    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }

  private void addpointchart() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastpoint++;
                series.appendData(new DataPoint(lastpoint, sredniaakce - 9.81), false, 100);
            }
        }, 5);
    }
    public void koniectest() {
        startTime = System.currentTimeMillis();
        stopTime = System.currentTimeMillis();
        long diff = stopTime - startTime;
        double czas = diff/1000;
        //diff = TimeUnit.MILLISECONDS.toSeconds(diff);
       czasham.setText("Czas całkowity: " + czas + " sekundy");




        for(int z= 0; z<pomiaryacceleration.length; z++)
        {
            if (pomiaryacceleration[z]-9.81>0){
                dsr++;
                suma=suma+pomiaryacceleration[z]-9.81;
            }
        }
        mfdd = suma/dsr;
        mfddtext.setText ("MFDD: " + new DecimalFormat("#.##").format(mfdd));
        double hamowanie = (mfdd*czas*czas)/2;
        sham.setText ("Sham: " + new DecimalFormat("#.##").format(hamowanie) + " m");
        koniec = true;
        runMDFF.setVisibility(View.GONE);



    }




}


