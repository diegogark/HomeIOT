package com.diegogark.homeiot.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.diegogark.homeiot.R;
import com.diegogark.homeiot.config.ConfiguracaoFirebase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class GraficoActivity extends AppCompatActivity {

    private LineChart grafico;
    private DatabaseReference dbFire = ConfiguracaoFirebase.getFirebaseDatabase();
    private String sensor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        grafico = findViewById(R.id.grafico);
        grafico.setViewPortOffsets(150, 100, 8, 100);
        grafico.setBackgroundColor(Color.rgb(231, 231, 231));

        grafico.setTouchEnabled(true);
        grafico.setDragEnabled(true);
        grafico.setScaleEnabled(true);
        grafico.setPinchZoom(true);

        XAxis x = grafico.getXAxis();
        x.setEnabled(true);
        x.setLabelCount(4, false);
        x.setValueFormatter(new ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());

            @Override
            public String getFormattedValue(float value) {

                long millis = (long) value;
                return mFormat.format(new Date(millis));
            }
        });
        final YAxis y = grafico.getAxisLeft();
        y.setLabelCount(5, false);
        y.setTextColor(Color.BLACK);
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y.setDrawGridLines(true);
        y.setAxisLineColor(Color.BLACK);
        y.setTextColor(Color.BLUE);
        y.setTextSize(12);
        grafico.getAxisRight().setEnabled(false);
        grafico.getLegend().setEnabled(true);
        grafico.animateXY(1000, 1000);
        Bundle bundle = getIntent().getExtras();
        sensor = bundle.getString("sensor");
        final List<Entry> dados = new ArrayList<>();
        dbFire.child("DB").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long xData = 0;
                float yValor;
                dados.clear();
                for (DataSnapshot d: snapshot.getChildren()){
                    xData = Long.parseLong(d.getKey())*1000;
                    yValor = d.child(sensor).getValue(Float.class);
                    dados.add(new Entry(xData, yValor));
                    Log.i("hora", xData + "-" + yValor);
                }
                Toast.makeText(getApplicationContext(), "passou", Toast.LENGTH_SHORT).show();
                LineDataSet dataSet = new LineDataSet(dados, sensor);
                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataSet.setCubicIntensity(0.1f);
                dataSet.setDrawFilled(true);
                dataSet.setDrawCircles(true);
                dataSet.setLineWidth(1.8f);
                dataSet.setCircleRadius(2f);
                dataSet.setCircleColor(Color.WHITE);
                dataSet.setHighLightColor(Color.rgb(244, 117, 117));
                dataSet.setColor(Color.BLACK);
                dataSet.setFillColor(Color.BLUE);
                dataSet.setFillAlpha(80);
                dataSet.setValueTextSize(10);
                dataSet.setDrawHorizontalHighlightIndicator(true);
                dataSet.setFillFormatter(new IFillFormatter() {
                    @Override
                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                        return grafico.getAxisLeft().getAxisMinimum();
                    }
                });
                LineData data = new LineData(dataSet);
                grafico.setData(data);
                grafico.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}