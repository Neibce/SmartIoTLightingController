package com.tcd2019.iotcontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    volatile TCPClient mTCPClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));
            }
        }

        final Switch swMode = findViewById(R.id.swMode);
        CardView cardViewMode = findViewById(R.id.cardViewMode);
        cardViewMode.setOnClickListener(new CardView.OnClickListener(){
            @Override
            public void onClick(View view) {
                swMode.performClick();
            }
        });


        final ToggleButton btnLightManual1 = findViewById(R.id.btnLightManual1);
        final ToggleButton btnLightManual2 = findViewById(R.id.btnLightManual2);
        final ToggleButton btnLightManual3 = findViewById(R.id.btnLightManual3);
        final ToggleButton btnLightManual4 = findViewById(R.id.btnLightManual4);
        final ToggleButton btnLightManual5 = findViewById(R.id.btnLightManual5);

        final CardView cvLightManual = findViewById(R.id.cvLightManual);

        swMode.setChecked(false);
        cvLightManual.setVisibility(View.GONE);

        swMode.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    swMode.setText("수동");
                    cvLightManual.setVisibility(View.VISIBLE);
                    mTCPClient.sendMessage("{ \"to\" : \"esp\", \"type\" : \"mode\", \"manual\" : 1 }");
                    btnLightManual1.setChecked(false);
                    btnLightManual2.setChecked(false);
                    btnLightManual3.setChecked(false);
                    btnLightManual4.setChecked(false);
                    btnLightManual5.setChecked(false);
                }else{
                    swMode.setText("자동");
                    cvLightManual.setVisibility(View.GONE);
                    mTCPClient.sendMessage("{ \"to\" : \"esp\", \"type\" : \"mode\", \"manual\" : 0 }");
                }
            }
        });

        btnLightManual1.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (mTCPClient != null) {
                    mTCPClient.sendMessage("{ \"to\" : \"esp\", \"type\" : \"light\", \"num\" : 1, \"mode\" : "  + ((isChecked) ? 1 : 0) + " }");
                }
            }
        });
        btnLightManual2.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (mTCPClient != null) {
                    mTCPClient.sendMessage("{ \"to\" : \"esp\", \"type\" : \"light\", \"num\" : 2, \"mode\" : "  + ((isChecked) ? 1 : 0) + " }");
                }
            }
        });
        btnLightManual3.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (mTCPClient != null) {
                    mTCPClient.sendMessage("{ \"to\" : \"esp\", \"type\" : \"light\", \"num\" : 3, \"mode\" : "  + ((isChecked) ? 1 : 0) + " }");
                }
            }
        });
        btnLightManual4.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (mTCPClient != null) {
                    mTCPClient.sendMessage("{ \"to\" : \"esp\", \"type\" : \"light\", \"num\" : 4, \"mode\" : "  + ((isChecked) ? 1 : 0) + " }");
                }
            }
        });

        btnLightManual5.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (mTCPClient != null) {
                    mTCPClient.sendMessage("{ \"to\" : \"esp\", \"type\" : \"light\", \"num\" : 5, \"mode\" : "  + ((isChecked) ? 1 : 0) + " }");
                }
            }
        });

        chartInit();
        new ConnectTask().execute("");
    }

    public class ConnectTask extends AsyncTask<String, String, TCPClient> {
        @Override
        protected TCPClient doInBackground(String... message) {
            //we create a TCPClient object
            mTCPClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    Log.d("test", "response " + message);
                    publishProgress(message);
                }
            });
            mTCPClient.run();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            try {
                JSONObject jsonObject = new JSONObject(values[0]);
                if(jsonObject.getString("to").equals("phone")){
                    if(jsonObject.getString("type").equals("person")){
                        TextView tvPerson = findViewById(R.id.tvPerson);
                        tvPerson.setText(jsonObject.getString("num") + "명");
                    }else if(jsonObject.getString("type").equals("watt")){
                        TextView tvPerson = findViewById(R.id.tvWatt);
                        tvPerson.setText(jsonObject.getString("num") + "W");
                    }else if(jsonObject.getString("type").equals("cds")) {
                        TextView tvPerson = findViewById(R.id.tvCds);
                        tvPerson.setText(jsonObject.getString("num") + "%");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //process server response here....
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mTCPClient != null) {
            mTCPClient.stopClient();
        }
    }

    private void chartInit(){
        LineChart lineChart = findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 0));
        entries.add(new Entry(1, 0));
        entries.add(new Entry(2, 0));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 0));
        entries.add(new Entry(5, 0));
        entries.add(new Entry(6, 10));
        entries.add(new Entry(7, 9));
        entries.add(new Entry(8, 8));
        entries.add(new Entry(9, 6));
        entries.add(new Entry(10, 4));
        entries.add(new Entry(11, 2));
        entries.add(new Entry(12, 2));
        entries.add(new Entry(13, 2));
        entries.add(new Entry(14, 3));
        entries.add(new Entry(15, 4));
        entries.add(new Entry(16, 5));
        entries.add(new Entry(17, 6));
        entries.add(new Entry(18, 6));
        entries.add(new Entry(19, 6));
        entries.add(new Entry(20, 6));
        entries.add(new Entry(21, 7));
        entries.add(new Entry(22, 8));
        entries.add(new Entry(23, 10));

        LineDataSet lineDataSet = new LineDataSet(entries,null);
        lineDataSet.setLineWidth(1);
        lineDataSet.setCircleRadius(3);
        lineDataSet.setCircleColor(Color.parseColor("#FFFFFFFF"));
        lineDataSet.setCircleHoleColor(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFFFFFFF"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setDrawLabels(true);
        xAxis.setLabelCount(24);
        xAxis.setDrawGridLines(false);
        xAxis.setGridColor(Color.WHITE);
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.WHITE);
        yLAxis.setAxisLineColor(Color.WHITE);
        yLAxis.setGridColor(Color.WHITE);
        yLAxis.setLabelCount(4);
        yLAxis.setDrawGridLines(false);
        yLAxis.setDrawLabels(false);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(true);
        yRAxis.setDrawGridLines(true);
        yRAxis.setAxisLineColor(Color.WHITE);
        yRAxis.setGridColor(Color.WHITE);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EaseInCubic);
        lineChart.invalidate();
    }
}
