package com.dfrobot.angelo.blunobasicdemo;

 import java.util.ArrayList;
 import com.github.mikephil.charting.charts.LineChart;
 import com.github.mikephil.charting.components.Legend;
 import com.github.mikephil.charting.components.Legend.LegendForm;
 import com.github.mikephil.charting.components.YAxis;
 import com.github.mikephil.charting.data.Entry;
 import com.github.mikephil.charting.data.LineData;
 import com.github.mikephil.charting.data.LineDataSet;
 import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
 import com.github.mikephil.charting.utils.ColorTemplate;

 import android.app.Activity;
 import android.support.v7.app.ActionBarActivity;
 import android.graphics.Color;
 import android.os.Bundle;

public class pressLineChart extends Activity {

    private LineChart mLineChart;
//  private Typeface mTf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLineChart = (LineChart) findViewById(R.id.line_chart);
        mLineChart.setTouchEnabled(true);
        // enable scaling and dragging
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setDrawGridBackground(false);
        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(true);
        // set an alternative background color
        mLineChart.setBackgroundColor(Color.GRAY);
//      mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");

        LineData mLineData = getLineData(100, 100);
        mLineData.notifyDataChanged();
        mLineChart.setData(mLineData);
        mLineChart.setVisibleXRangeMaximum(20);
       // mLineChart.invalidate();
        // let the chart know it's data has changed
        mLineChart.notifyDataSetChanged();
        // limit the number of visibllineDataSetse entries
        mLineChart.setVisibleXRangeMaximum(5);
        // mChart.setVisibleYRange(30, AxisDependency.LEFT);
        // move to the latest entry
        mLineChart.moveViewToX(mLineData.getEntryCount());
        //showChart(mLineChart, mLineData, Color.GRAY);
    }

    // 设置显示的样式
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false);  //是否在折线图上添加边框

        // no description text
        lineChart.setDescription("");// 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable / disable grid background
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        // enable touch gestures
        lineChart.setTouchEnabled(true); // 设置是否可以触摸

        // enable scaling and dragging
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(true);// 是否可以缩放

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);//
        lineChart.setVisibleXRangeMaximum(20);

        lineChart.setBackgroundColor(color);// 设置背景

        // add data
        lineChart.setData(lineData); // 设置数据

        // get the legend (only possible after setting data)
        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

        // modify the legend ...
        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.WHITE);// 颜色
//      mLegend.setTypeface(mTf);// 字体

        lineChart.animateX(2500); // 立即执行的动画,x轴
    }

    /**
     * add an random entry
     */
    private void addEntry() {

        LineData data = mLineChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 40) + 30f), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            mLineChart.notifyDataSetChanged();

            // limit the number of visibllineDataSetse entries
            mLineChart.setVisibleXRangeMaximum(5);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mLineChart.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }
    /**
     * create linear type data set
     *
     * @return set
     */
    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(6f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(10f);
        set.setDrawValues(false);
        return set;
    }

    private LineData getLineData(int count, float range) {
      /*  ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add("" + i);
        }*/
        ArrayList<Entry> xValues = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float value = (float) (Math.random() * range) + 3;
             xValues.add(new Entry(i, value));
        }

        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(xValues, "实时压力数据"
);

        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        //用y轴的集合来设置参数
      //  lineDataSet.setLineWidth(4f); // 线宽

        //lineDataSet.setCircleSize();// 显示的圆形大小
      //  lineDataSet.setCircleHoleRadius(6f);
      //  lineDataSet.setColor(Color.BLUE);// 显示颜色
       // lineDataSet.setCircleColor(Color.BLACK);// 圆形的颜色
      //  lineDataSet.setHighLightColor(Color.BLUE); // 高亮的线的颜色

        lineDataSet.setColor(ColorTemplate.getHoloBlue());
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(6f);
        lineDataSet.setFillAlpha(65);
        lineDataSet.setFillColor(ColorTemplate.getHoloBlue());
        lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setValueTextSize(10f);
        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // add the datasets

        // create a data object with the datasets

        LineData lineData = new LineData(lineDataSet);
        return lineData;
    }
}
