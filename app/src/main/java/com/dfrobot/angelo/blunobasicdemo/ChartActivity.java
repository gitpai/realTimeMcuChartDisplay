package com.dfrobot.angelo.blunobasicdemo;

import android.app.Activity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yujie on 2017/4/29.
 */

public class ChartActivity extends Activity {
    private LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lineChart = (LineChart) findViewById(R.id.line_chart);


        List<Entry> entries = new ArrayList<Entry>();

        for (int i=0;i<10;i++) {

            // turn your data into Entry objects
            entries.add(new Entry(i, i+1));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(200);
        dataSet.setValueTextColor(50); // styling, ...
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); //
    }
}

