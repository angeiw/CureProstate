package com.redcity.cureprostate.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.redcity.cureprostate.Protocol;
import com.redcity.cureprostate.R;
import com.redcity.cureprostate.mvp.view.ISerialPort;
import com.redcity.cureprostate.service.IService;
import com.redcity.cureprostate.service.SerialPortService;
import com.redcity.cureprostate.util.Constant;
import com.redcity.cureprostate.vo.ParamVo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by COREIGHT-101 on 2018/8/20.
 */

public class SerialPortActivity extends Activity {

    private final String TAG = SerialPortActivity.class.getSimpleName();
    Unbinder unbinder;
    @Nullable@BindView(R.id.tv_electricity) TextView tv_electricity;
    @Nullable@BindView(R.id.tv_voltage) TextView tv_voltage;
    @Nullable@BindView(R.id.tv_coulomb) TextView tv_coulomb;
    @Nullable@BindView(R.id.et_inputElectricity) EditText inputElectricity;
    @Nullable@BindView(R.id.et_inputVoltage) EditText inputVoltage;
    @Nullable@BindView(R.id.et_inputCoulomb) EditText inputCoulomb;
    @Nullable@BindView(R.id.et_electricityRate) EditText inputElectricityRate;
    @Nullable@BindView(R.id.chart_electricity) LineChart chart_electricity;
    @Nullable@BindView(R.id.chart_voltage) LineChart chart_voltage;
    @Nullable@BindView(R.id.chart_coulomb) LineChart chart_coulomb;
    @Nullable@BindView(R.id.historicalHideLayout) LinearLayout historicalHideLayout;
    @Nullable@BindView(R.id.progress_coulomb) RoundCornerProgressBar progress_coulomb;
    @Nullable@BindView(R.id.btn_switch) Button btn_switch;
    @Nullable@BindView(R.id.historicalButtonLayout) LinearLayout historicalButtonLayout;

    Intent intent;
    boolean isStart = false;
    IService iService;
    UIBroadcastReceiver UIreceiver;
    private LFilePicker filePicker;
    int REQUESTCODE_FROM_ACTIVITY = 1000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main2);
        unbinder = ButterKnife.bind(this);
        intent = new Intent(this, SerialPortService.class);
        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);

        initChart(chart_electricity,null,"E");
        initChart(chart_voltage,null,"V");
        initChart(chart_coulomb,null,"C");

        UIreceiver = new UIBroadcastReceiver();
        registerReceiver(UIreceiver,makeIntentFilter());


       filePicker = new LFilePicker()
                .withActivity(this)
                .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                .withStartPath("/storage/emulated/0/cureProstate/姓名+ID")
                .withIsGreater(true)
                .withMutilyMode(false)
                .withFileFilter(new String[]{".dat",".txt"});


    }

     private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iService = (IService) iBinder;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    private void initChart(LineChart mChart, ArrayList<Entry> values, String label){
        // enable description text
        mChart.getDescription().setEnabled(false);
        // enable touch gestures
        mChart.setTouchEnabled(true);
        // enable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleXEnabled(true);
        mChart.setScaleYEnabled(false);
        mChart.setDrawGridBackground(false);
        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);
        // set an alternative background color
        mChart.setBackgroundColor(Color.TRANSPARENT);
        // no description text
        mChart.getDescription().setEnabled(false);
        if (values != null) {
            setData(mChart, values, label);
            mChart.animateX(2500);
        }else{
            LineData data = new LineData();
            data.setValueTextColor(Color.WHITE);
            // add empty data
            mChart.setData(data);
        }

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);
        // modify the legend ...
//        l.setForm(Legend.LegendForm.LINE);
//        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setTextSize(11f);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setDrawAxisLine(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        if (label.equals("E")){
            leftAxis.setAxisMaximum(120f);
        }else if (label.equals("V")){
            leftAxis.setAxisMaximum(12f);
        }else {
            leftAxis.setAxisMaximum(1000f);
        }
//        leftAxis.setAxisMaximum(maxYAxis);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void setData(LineChart mChart,ArrayList<Entry> values,String label) {

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);

            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        }
        else {
            set1= new LineDataSet(values, label);
            set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            set1.setCubicIntensity(0.2f);
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setDrawCircles(false);
            set1.setLineWidth(2f);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawValues(false);
            if (label.equals("E")){
                set1.setColor(Color.parseColor("#00a0e9"));
            }else if (label.equals("C")){
                set1.setColor(Color.parseColor("#981cbe"));
            }else{
                set1.setColor(Color.parseColor("#f8b551"));
            }

            // create a data object with the datasets
            LineData data = new LineData(set1);
//            data.setValueTextColor(Color.WHITE);
//            data.setValueTextSize(9f);

            // set data
            mChart.setData(data);


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        unbindService(serviceConnection);
        unregisterReceiver(UIreceiver);
    }

    @OnClick(R.id.btn_playback)
    void btn_playback(View view){

        if (isStart){
            Toast.makeText(this,"正在治疗中...",Toast.LENGTH_SHORT).show();
            return;
        }
        //显示已有数据的列表,将currentFile更改为选中的文件

        filePicker.start();


    }



    @OnClick(R.id.btn_switch)
    void btn_switch(View view){
        if (!isStart){
            if (TextUtils.isEmpty(inputElectricity.getText().toString())||TextUtils.isEmpty(inputVoltage.getText().toString())||TextUtils.isEmpty(inputCoulomb.getText().toString())||TextUtils.isEmpty(inputElectricityRate.getText().toString())){
                iService.sendData(Protocol.UTRS_READ_CURE_PARAMETER,null);//获取参数
            }else{
                short electricityParamer = (short) (Float.valueOf(inputElectricity.getText().toString()) * 100);
                short voltageParamer = (short) (Float.valueOf(inputVoltage.getText().toString()) * 100);
                int coulombParamer = (int) (Float.valueOf(inputCoulomb.getText().toString()) * 1000);
                short electricityRateParamer = (short) (Float.valueOf(inputElectricityRate.getText().toString()) * 1000);
                if (iService.verifyParameter(electricityParamer,voltageParamer,coulombParamer,electricityRateParamer)){
                    ParamVo paramVo = new ParamVo();
                    paramVo.electricityParamer =electricityParamer;
                    paramVo.voltageParamer = voltageParamer;
                    paramVo.coulombParamer = coulombParamer;
                    paramVo.electricityRateParamer = electricityRateParamer;
                    iService.sendData(Protocol.UTRS_WRITE_CURE_PARAMETER,paramVo);//设置参数
                }
            }
        }else{
            iService.sendData(Protocol.UTRS_WRITE_END_CURE,null);
        }
    }

    private void addEntry(LineChart mChart,String label,float i) {

        LineData data = mChart.getData();

        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
//            ILineDataSet set1 = data.getDataSetByIndex(1);
            // set.addEntry(...); // can be called as well
            if (set == null) {
                set = createLineDataSet(label);
                data.addDataSet(set);
            }
            data.addEntry(new Entry(set.getEntryCount(), i), 0);

            data.notifyDataChanged();
            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            mChart.setVisibleXRangeMaximum(50);
            // move to the latest entry
            Log.d(TAG,"getEntryCount...."+data.getEntryCount());
            mChart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createLineDataSet(String label) {
        LineDataSet set = new LineDataSet(null, null);
        set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setDrawCircles(false);
        set.setLineWidth(2f);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setDrawValues(false);
        if (label.equals("E")){
            set.setColor(Color.parseColor("#00a0e9"));
        }else if (label.equals("C")){
            set.setColor(Color.parseColor("#981cbe"));
        }else{
            set.setColor(Color.parseColor("#f8b551"));
        }
        return set;
    }

    class UIBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constant.startCureAction)){
                btn_switch.setText("停止");
                startCurseUI();
                clear();
                isStart = true;
            }
            else if (action.equals(Constant.readParameterAction)){
                inputElectricity.setText(intent.getFloatExtra("E",-1)+"");
                inputVoltage.setText(intent.getFloatExtra("V",-1)+"");
                inputCoulomb.setText(intent.getFloatExtra("C",-1)+"");
                inputElectricityRate.setText(intent.getFloatExtra("ER",-1)+"");
                iService.sendData(Protocol.UTRS_WRITE_START_CURE,null);

            }
            else if (action.equals(Constant.writeParameterAction)){
                iService.sendData(Protocol.UTRS_WRITE_START_CURE,null);
            }
            else if (action.equals(Constant.receiveDataAction)){
//                isStart = true;
                tv_electricity.setText(intent.getFloatExtra("electricity",-1)+"");
                tv_voltage.setText(intent.getFloatExtra("voltage",-1)+"");
                tv_coulomb.setText(intent.getFloatExtra("coulomb",-1)+"");
                addEntry(chart_electricity,"E",intent.getFloatExtra("electricity",-1));
                addEntry(chart_voltage,"V",intent.getFloatExtra("voltage",-1));
                progress_coulomb.setProgress(intent.getIntExtra("coulombProgress",-1));

            }
            else if (action.equals(Constant.stopCureAction)){
                btn_switch.setText("停止中...");
                btn_switch.setEnabled(false);
            }
            else if (action.equals(Constant.finishCureAction)){
                btn_switch.setText("治疗");
                btn_switch.setEnabled(true);
                finishCureUI();
                clear();
                isStart = false;
            }

        }
    }


    private void startCurseUI(){
        inputElectricity.setEnabled(false);
        inputVoltage.setEnabled(false);
        inputCoulomb.setEnabled(false);
        inputElectricityRate.setEnabled(false);

    }
    private void finishCureUI(){
        inputElectricity.setEnabled(true);
        inputVoltage.setEnabled(true);
        inputCoulomb.setEnabled(true);
        inputElectricityRate.setEnabled(true);
        inputElectricity.setText("");
        inputVoltage.setText("");
        inputCoulomb.setText("");
        inputElectricityRate.setText("");
       tv_electricity.setText("99.99");
        tv_voltage.setText("9.99");
        tv_coulomb.setText("999.999");

    }

    private void clear(){
        chart_electricity.clear();
        chart_electricity.notifyDataSetChanged();
        chart_voltage.clear();
        chart_voltage.notifyDataSetChanged();
        LineData lineData = new LineData();
        // add empty data
        chart_electricity.setData(lineData);
        LineData lineData1 = new LineData();
        chart_voltage.setData(lineData1);
        progress_coulomb.setProgress(0);
    }


    private IntentFilter makeIntentFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.receiveDataAction);
        intentFilter.addAction(Constant.readParameterAction);
        intentFilter.addAction(Constant.writeParameterAction);
        intentFilter.addAction(Constant.startCureAction);
        intentFilter.addAction(Constant.stopCureAction);
        intentFilter.addAction(Constant.finishCureAction);
        return intentFilter;
    }
}
