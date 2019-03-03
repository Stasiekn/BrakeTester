package com.example.lne.braketester;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static com.example.lne.braketester.MainView.dsr;
import static com.example.lne.braketester.MainView.pomiaryacceleration;
import static com.example.lne.braketester.MainView.pomiarypredkosc;


public class Wykresy extends AppCompatActivity  {
    private Handler mHandler = new Handler();
    private LineGraphSeries<DataPoint> series1, series2;
    private double lastpoint = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wykresy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setLogo(R.mipmap.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        GraphView graph = (GraphView) findViewById(R.id.graph1);
        series1 = new LineGraphSeries<>(new DataPoint[]{});
        series2 = new LineGraphSeries<>(new DataPoint[]{});
        series2.setColor(Color.YELLOW);
        graph.addSeries(series1);
        graph.addSeries(series2);
        graph.getViewport().setMaxY(10);
        graph.getViewport().setMinY(-10);
        graph.getViewport().setYAxisBoundsManual(false);
        //graph.getViewport().setScalable(false);
        //graph.getViewport().setScalableY(true);

        for (int i=0; i<dsr; i++){
            lastpoint++;
            series1.appendData(new DataPoint(lastpoint, pomiaryacceleration[i]), false, 2000);
            series2.appendData(new DataPoint(lastpoint, pomiarypredkosc[i]), false, 2000);

        }





    }
}
