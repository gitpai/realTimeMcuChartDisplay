package com.dfrobot.angelo.blunobasicdemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity  extends BlunoLibrary {
	private Button buttonScan;
	private Button buttonStart;
	private Button buttonStop;
	private ImageButton buttonSerialSend;
	private EditText serialSendText;
	private TextView serialReceivedText;
	private String dateTemp="";
	private boolean startFlag=false;
	private boolean endFlag=false;
	private LineChart mLineChart;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		onCreateProcess();														//onCreate Process by BlunoLibrary


		serialBegin(115200);													//set the Uart Baudrate on BLE chip to 115200

		//serialReceivedText=(TextView) findViewById(R.id.serialReveicedText);	//initial the EditText of the received data
		serialSendText=(EditText) findViewById(R.id.serialSendText);			//initial the EditText of the sending data

		buttonStart = (Button) findViewById(R.id.buttonStart);		//initial the button for sending the data
		buttonStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//serialSend(serialSendText.getText().toString());				//send the data to the BLUNO
				serialSend("start");
			}
		});
		buttonStop = (Button) findViewById(R.id.buttonStop);		//initial the button for sending the data
		buttonStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//serialSend(serialSendText.getText().toString());				//send the data to the BLUNO
				serialSend("stop");
			}
		});

		buttonScan = (Button) findViewById(R.id.buttonScan);					//initial the button for scanning the BLE device
		buttonScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				buttonScanOnClickProcess();										//Alert Dialog for selecting the BLE device
			}
		});
		/*
			图表界面
		 */

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
		LineData data = new LineData();
		data.setValueTextColor(Color.WHITE);

		// add empty data
		mLineChart.setData(data);


		/*LineData mLineData = getLineData(100, 100);

		mLineChart.setData(mLineData);

		mLineData.notifyDataChanged();
		// mLineChart.invalidate();
		// let the chart know it's data has changed
		mLineChart.notifyDataSetChanged();
		// limit the number of visibllineDataSetse entries
		mLineChart.setVisibleXRangeMaximum(5);
		// mChart.setVisibleYRange(30, AxisDependency.LEFT);
		// move to the latest entry
		mLineChart.moveViewToX(mLineData.getEntryCount());*/

	}

	protected void onResume(){
		super.onResume();
		System.out.println("BlUNOActivity onResume");
		onResumeProcess();														//onResume Process by BlunoLibrary
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();
		onPauseProcess();														//onPause Process by BlunoLibrary
	}

	protected void onStop() {
		super.onStop();
		onStopProcess();														//onStop Process by BlunoLibrary
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		onDestroyProcess();														//onDestroy Process by BlunoLibrary
	}

	@Override
	public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
		switch (theConnectionState) {											//Four connection state
			case isConnected:
				buttonScan.setText("Connected");
				break;
			case isConnecting:
				buttonScan.setText("Connecting");
				break;
			case isToScan:
				buttonScan.setText("Scan");
				break;
			case isScanning:
				buttonScan.setText("Scanning");
				break;
			case isDisconnecting:
				buttonScan.setText("isDisconnecting");
				break;
			default:
				break;
		}
	}

	@Override
	public void onSerialReceived(String theString) {

		if(theString.startsWith("{")){
			startFlag=true;
			Log.d("test", "theString.startsWith('{')"+theString);
		}
		if(startFlag){
			if (theString.endsWith("\r\n")){

				startFlag=false;
				endFlag=true;
				Log.d("test", "theString.endWith('}')"+theString);
			}
			dateTemp+=theString;
		}
		if(endFlag){
			try {
				JSONObject  dataJson=new JSONObject(dateTemp);
				String	N= dataJson.getString("N");
				String I= dataJson.getString("l");
				String m= dataJson.getString("m");
				String r= dataJson.getString("r");
				int P1=Integer.parseInt(I);
				int P2=Integer.parseInt(m);
				int P3=Integer.parseInt(r);
				addEntry(P1,P2,P3);
				System.out.println(" N："+N+"I: "+I+"m: "+m+"r: "+r);
				serialSendText.setText(" N："+N+"I: "+I+"m: "+m+"r: "+r);
				dateTemp="";
				endFlag=false;
			} catch (Exception e) {
				System.out.println("系统解析数据发生错误");
			}

		}

		//Once connection data received, this function will be called
		// TODO Auto-generated method stub
	//	serialReceivedText.append(theString);							//append the text into the EditText
		//The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
	//	((ScrollView)serialReceivedText.getParent()).fullScroll(View.FOCUS_DOWN);
	}
	/*
		图标函数
	 */ private LineData getLineData(int count, float range) {
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
	private void addEntry(int P1,int P2,int P3) {
		ArrayList<LineDataSet> allLinesList = new ArrayList<LineDataSet>();
		LineData data = mLineChart.getData();


		if (data != null) {

			ILineDataSet set1 = data.getDataSetByIndex(0);
			ILineDataSet set2 = data.getDataSetByIndex(1);
			ILineDataSet set3 = data.getDataSetByIndex(2);
			//set.addEntry(...); // can be called as well

			if (set1 == null) {
				set1 = createSet();
				data.addDataSet(set1);
			}
			if (set2 == null) {
				set2 = createSet();
				data.addDataSet(set2);
			}
			if (set3 == null) {
				set3 = createSet();
				data.addDataSet(set3);
			}

			//set1.addEntry(new Entry(set1.getEntryCount(), i));
			//set2.addEntry(new Entry(set2.getEntryCount(), i+10));
			data.addEntry(new Entry(set1.getEntryCount(), P1), 0);
			data.addEntry(new Entry(set2.getEntryCount(), P2), 1);
			data.addEntry(new Entry(set3.getEntryCount(), P3), 2);


			// let the chart know it's data has changed
			mLineChart.notifyDataSetChanged();

			// limit the number of visibllineDataSetse entries
			mLineChart.setVisibleXRangeMaximum(10);
			// mChart.setVisibleYRange(30, AxisDependency.LEFT);

			// move to the latest entry
			mLineChart.moveViewToX(data.getEntryCount());

			// this automatically refreshes the chart (calls invalidate())
			// mChart.moveViewTo(data.getXValCount()-7, 55f,
			// AxisDependency.LEFT);
		}
	}

	private LineDataSet createSet() {
		LineDataSet set = new LineDataSet(null, "Real-time sensor data");
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
}