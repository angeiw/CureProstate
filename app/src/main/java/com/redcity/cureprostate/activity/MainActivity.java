//package com.redcity.cureprostate.activity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
//import com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar;
//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
//import com.github.mikephil.charting.utils.ColorTemplate;
//import com.leon.lfilepickerlibrary.LFilePicker;
//import com.leon.lfilepickerlibrary.utils.Constant;
//import com.redcity.cureprostate.Protocol;
//import com.redcity.cureprostate.R;
//import com.redcity.cureprostate.mvp.presenter.SerialPortPresenter;
//import com.redcity.cureprostate.mvp.view.ISerialPort;
//import com.redcity.cureprostate.util.FileUtils;
//import com.redcity.cureprostate.vo.ReceiveVo;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//
///**
// * Created by COREIGHT-101 on 2018/4/12.
// */
//
//public class MainActivity extends Activity implements ISerialPort{
//    private final String TAG = MainActivity.class.getSimpleName();
//    Unbinder unbinder;
//    @Nullable@BindView(R.id.tv_electricity) TextView tv_electricity;
//    @Nullable@BindView(R.id.tv_voltage) TextView tv_voltage;
//    @Nullable@BindView(R.id.tv_coulomb) TextView tv_coulomb;
//    @Nullable@BindView(R.id.et_inputElectricity) EditText inputElectricity;
//    @Nullable@BindView(R.id.et_inputVoltage) EditText inputVoltage;
//    @Nullable@BindView(R.id.et_inputCoulomb) EditText inputCoulomb;
//    @Nullable@BindView(R.id.et_electricityRate) EditText inputElectricityRate;
//    @Nullable@BindView(R.id.chart_electricity) LineChart chart_electricity;
//    @Nullable@BindView(R.id.chart_voltage) LineChart chart_voltage;
//    @Nullable@BindView(R.id.chart_coulomb) LineChart chart_coulomb;
//    @Nullable@BindView(R.id.historicalHideLayout) LinearLayout historicalHideLayout;
//    @Nullable@BindView(R.id.progress_coulomb) RoundCornerProgressBar progress_coulomb;
//    @Nullable@BindView(R.id.btn_switch) Button btn_switch;
//    @Nullable@BindView(R.id.historicalButtonLayout) LinearLayout historicalButtonLayout;
//
//    private boolean isStart = false;
//    private boolean isLocal;
//    private SerialPortPresenter mSerialPortPresenter;
//    private Thread thread;
//    private LFilePicker filePicker;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act_main2);
//        unbinder = ButterKnife.bind(this);
//        mSerialPortPresenter = new SerialPortPresenter(this,this);
//        mSerialPortPresenter.checkUSB();
//
//        initChart(chart_electricity,null,"E");
//        initChart(chart_voltage,null,"V");
//        initChart(chart_coulomb,null,"C");
//        filePicker = new LFilePicker()
//                .withActivity(MainActivity.this)
//                .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
//                .withStartPath("/storage/emulated/0/cureProstate/姓名+ID")
//                .withIsGreater(true)
//                .withMutilyMode(false)
//                .withFileFilter(new String[]{".dat",".txt"});
//                progress_coulomb.setOnProgressChangedListener(new BaseRoundCornerProgressBar.OnProgressChangedListener() {
//            @Override
//            public void onProgressChanged(int i, float v, boolean b, boolean b1) {
////                Log.w(TAG,"progressChanged..i.."+i+"v.."+v+"b.."+b+"b1.."+b1);
//                if (v == progress_coulomb.getMax()){
//                    mSerialPortPresenter.stop();
//                }
//            }
//        });
//
//        handler = new MyHandler(this);
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mSerialPortPresenter.checkConnected();
//    }
//
//    @Override
//    protected void onDestroy() {
//        mSerialPortPresenter.endSerial();
//        super.onDestroy();
//        handler.removeCallbacksAndMessages(null);
//        unbinder.unbind();
//    }
//
//    @OnClick(R.id.btn_switch)
//    public void btn_switch(View view){
//        if (!isStart){
//            FileUtils.maxElectricity = inputElectricity.getText().toString();
//            FileUtils.maxVoltage = inputVoltage.getText().toString();
//            FileUtils.maxCoulomb = inputCoulomb.getText().toString();
//            isLocal = false;
////            mSerialPortPresenter.openUsbSerial();
//            mSerialPortPresenter.writeDataToSerial(Protocol.startCure(),null);
//        }else{
//            mSerialPortPresenter.writeDataToSerial(Protocol.stopCure(),null);
//        }
//    }
//    int REQUESTCODE_FROM_ACTIVITY = 1000;
//    @OnClick(R.id.btn_playback)
//    public void btn_playback(View view){
//        if (isStart){
//            Toast.makeText(this,"正在治疗中...",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        //显示已有数据的列表,将currentFile更改为选中的文件
//
//        filePicker.start();
//
////        isLocal = true;
////        mSerialPortPresenter.readDataFromLocal();//防止无文件错误，可以去掉
////        historicalData();//读取历史数据方法
////        FileUtils.initCacheFile(this);
////        FileUtils.readToSDCard();
//    }
//
//    @OnClick(R.id.btn_exitHistorical)
//    public void btn_exitHistorical(View view){
//        cureView();
//        clear();
//    }
//
//    @OnClick(R.id.btn_selectHistorical)
//    public void btn_selectHistorical(View view){
//        filePicker.start();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
//
//                //If it is a file selection mode, you need to get the path collection of all the files selected
//                //List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);//Constant.RESULT_INFO == "paths"
//                List<String> list = data.getStringArrayListExtra("paths");
//                Log.d(TAG,"list....."+list.toString());
//                FileUtils.currentFile =  new File(list.get(0));
//                historyView();
//                isLocal = true;
////                mSerialPortPresenter.readDataFromLocal();//防止无文件错误，可以去掉
//                historicalData();//读取历史数据方法
//            }
//        }
//    }
//
//    private void cureView(){
//        inputElectricity.setEnabled(true);
//        inputVoltage.setEnabled(true);
//        inputCoulomb.setEnabled(true);
//        inputElectricityRate.setEnabled(true);
//        chart_coulomb.setVisibility(View.GONE);
//        historicalButtonLayout.setVisibility(View.GONE);
//        historicalHideLayout.setVisibility(View.VISIBLE);
//        inputElectricity.setText("");
//        inputVoltage.setText("");
//        inputCoulomb.setText("");
//        inputElectricityRate.setText("");
//        tv_electricity.setText("99.999");
//        tv_voltage.setText("9.99");
//        tv_coulomb.setText("999.999");
//
//    }
//
//    private void historyView(){
//        historicalHideLayout.setVisibility(View.GONE);
//        chart_coulomb.setVisibility(View.VISIBLE);
//        historicalButtonLayout.setVisibility(View.VISIBLE);
//        inputElectricity.setEnabled(false);
//        inputVoltage.setEnabled(false);
//        inputCoulomb.setEnabled(false);
//        inputElectricityRate.setEnabled(false);
//    }
//
//
//    private void initChart(LineChart mChart,ArrayList<Entry> values,String label){
//        // enable description text
//        mChart.getDescription().setEnabled(false);
//        // enable touch gestures
//        mChart.setTouchEnabled(true);
//        // enable scaling and dragging
//         mChart.setDragEnabled(false);
//        mChart.setScaleXEnabled(true);
//        mChart.setScaleYEnabled(false);
//        mChart.setDrawGridBackground(false);
//        // if disabled, scaling can be done on x- and y-axis separately
//        mChart.setPinchZoom(true);
//        // set an alternative background color
//        mChart.setBackgroundColor(Color.TRANSPARENT);
//        // no description text
//        mChart.getDescription().setEnabled(false);
////        LineData data = new LineData();
////        data.setValueTextColor(Color.WHITE);
////        // add empty data
////        mChart.setData(data);
//        if (values != null) {
//            setData(mChart, values, label);
//            mChart.animateX(2500);
//        }else{
//            LineData data = new LineData();
//            data.setValueTextColor(Color.WHITE);
//            // add empty data
//            mChart.setData(data);
//        }
//
//        // get the legend (only possible after setting data)
//        Legend l = mChart.getLegend();
//        l.setEnabled(false);
//        // modify the legend ...
////        l.setForm(Legend.LegendForm.LINE);
////        l.setTextColor(Color.WHITE);
//
//        XAxis xl = mChart.getXAxis();
//        xl.setTextSize(11f);
//        xl.setTextColor(Color.WHITE);
//        xl.setDrawGridLines(false);
//        xl.setDrawAxisLine(false);
//        xl.setAvoidFirstLastClipping(true);
//        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setTextColor(Color.WHITE);
//        if (label.equals("E")){
//            leftAxis.setAxisMaximum(120f);
//        }else if (label.equals("V")){
//            leftAxis.setAxisMaximum(12f);
//        }else {
//            leftAxis.setAxisMaximum(1000f);
//        }
////        leftAxis.setAxisMaximum(maxYAxis);
//        leftAxis.setAxisMinimum(0f);
//        leftAxis.setDrawGridLines(true);
//        YAxis rightAxis = mChart.getAxisRight();
//        rightAxis.setEnabled(false);
//
//    }
//
//    private LineDataSet createLineDataSet(String label) {
//        LineDataSet set = new LineDataSet(null, null);
//        set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
//        set.setCubicIntensity(0.2f);
//        set.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set.setDrawCircles(false);
//        set.setLineWidth(2f);
//        set.setHighLightColor(Color.rgb(244, 117, 117));
//        set.setDrawValues(false);
//        if (label.equals("E")){
//            set.setColor(Color.parseColor("#00a0e9"));
//        }else if (label.equals("C")){
//            set.setColor(Color.parseColor("#981cbe"));
//        }else{
//            set.setColor(Color.parseColor("#f8b551"));
//        }
//        return set;
//    }
//
//
//    private void addEntry(LineChart mChart,String label,float i) {
//
//        LineData data = mChart.getData();
//
//        if (data != null) {
//            ILineDataSet set = data.getDataSetByIndex(0);
////            ILineDataSet set1 = data.getDataSetByIndex(1);
//            // set.addEntry(...); // can be called as well
//            if (set == null) {
//                set = createLineDataSet(label);
//                data.addDataSet(set);
//            }
//            data.addEntry(new Entry(set.getEntryCount(), i), 0);
//
//            data.notifyDataChanged();
//            // let the chart know it's data has changed
//            mChart.notifyDataSetChanged();
//
//            mChart.setVisibleXRangeMaximum(50);
//            // move to the latest entry
//            Log.d(TAG,"getEntryCount...."+data.getEntryCount());
//            mChart.moveViewToX(data.getEntryCount());
//        }
//    }
//
//
//
//    @Override
//    public void noSupport() {
//        Toast.makeText(this, "No Support USB host API", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void noConnected() {
//        Toast.makeText(this, "no connected", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void noMoreDevices() {
//        Toast.makeText(this, "no more devices found", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void attached() {
//        Toast.makeText(this, "attached", Toast.LENGTH_SHORT).show();
//        mSerialPortPresenter.openUsbSerial();
//    }
//
//    @Override
//    public void noPermission() {
//        Toast.makeText(this, "cannot open, maybe no permission", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void chipNoSupport() {
//        Toast.makeText(this, "cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void connected() {
//        Toast.makeText(this, "connected : " , Toast.LENGTH_SHORT).show();
//        isStart = true;
//        if (thread != null && thread.isAlive()){
//            thread.interrupt();
//            thread = null;
//        }
////        thread = new Thread(readThread);
////        thread.start();
////        if (!isLocal){
////            //发送设置参数
////            mSerialPortPresenter.writeDataToSerial(Protocol.startCure(),3);
////        }
////        if (!isLocal){
////        //发送设置参数
////            short electricityParamer = (short) (Float.valueOf(inputElectricity.getText().toString()) * 100);
////            short voltageParamer = (short) (Float.valueOf(inputVoltage.getText().toString()) * 100);
////            int coulombParamer = (int) (Float.valueOf(inputCoulomb.getText().toString()) * 1000);
////            short electricityRateParamer = (short) (Float.valueOf(inputElectricityRate.getText().toString()) * 1000);
////            Log.d(TAG,"writeData...."+ Arrays.toString(Protocol.setParam(electricityParamer,voltageParamer,coulombParamer,electricityRateParamer)));
////            mSerialPortPresenter.writeDataToSerial(Protocol.setParam(electricityParamer,voltageParamer,coulombParamer,electricityRateParamer));
////        }
//    }
//
//    @Override
//    public void sendFailed() {
//        isStart = false;
//        handler.sendEmptyMessage(sendFailedCode);
//    }
//
//    @Override
//    public void start() {
//      handler.sendEmptyMessage(startCode);
//    }
//
//    @Override
//    public void treating() {
//        handler.sendEmptyMessage(treatingCode);
//
//    }
//
//    @Override
//    public void receive(final short voltage,final short electricity, final int coulomb, final short time) {
////        Log.d(TAG,"v.."+((float)voltage/100)+"..e.."+((float)electricity/100)+"..c.."+coulomb+"..t.."+time);
//        ReceiveVo vo = new ReceiveVo();
//        vo.voltage = voltage;
//        vo.electricity = electricity;
//        vo.coulomb = coulomb;
//        vo.time = time;
//        Message message = handler.obtainMessage();
//        message.what = receiveCode;
//        message.obj = vo;
//        handler.sendMessage(message);
//
//    }
//
//    @Override
//    public void stopping() {
//        handler.sendEmptyMessage(stoppingCode);
//
//    }
//
//    @Override
//    public void stop(){
//        handler.sendEmptyMessage(stopCode);
//    }
//
//    private void clear(){
//        chart_electricity.clear();
//        chart_electricity.notifyDataSetChanged();
//        chart_voltage.clear();
//        chart_voltage.notifyDataSetChanged();
//        LineData lineData = new LineData();
//        // add empty data
//        chart_electricity.setData(lineData);
//        LineData lineData1 = new LineData();
//        chart_voltage.setData(lineData1);
//        progress_coulomb.setProgress(0);
//    }
//
//    private void historicalData(){
//        try {
//            JSONArray jsonArray = mSerialPortPresenter.readFile();
//            ArrayList<Entry> voltageValues = new ArrayList<>();
//            ArrayList<Entry> electricityValues = new ArrayList<>();
//            ArrayList<Entry> coulombValues = new ArrayList<>();
//            for (int i = 0;i<jsonArray.length();i++){
//                Log.d(TAG,Float.valueOf(jsonArray.getJSONObject(i).get("voltage").toString())/100+"..V");
//                Log.d(TAG,Float.valueOf(jsonArray.getJSONObject(i).get("electricity").toString())/100+"..E");
//                Log.d(TAG,Integer.valueOf(jsonArray.getJSONObject(i).get("coulomb").toString())/1000+"..C");
//                voltageValues.add(new Entry(i,  Float.valueOf(jsonArray.getJSONObject(i).get("voltage").toString())/100, 0));
//                electricityValues.add(new Entry(i,  Float.valueOf(jsonArray.getJSONObject(i).get("electricity").toString())/100, 0));
//                coulombValues.add(new Entry(i,  Float.valueOf(jsonArray.getJSONObject(i).get("coulomb").toString())/100, 0));
//            }
//            tv_electricity.setText(Float.valueOf(jsonArray.getJSONObject(jsonArray.length()-1).get("electricity").toString())/100+"");
//            tv_voltage.setText(Float.valueOf(jsonArray.getJSONObject(jsonArray.length()-1).get("voltage").toString())/100+"");
//            tv_coulomb.setText(Float.valueOf(jsonArray.getJSONObject(jsonArray.length()-1).get("coulomb").toString())/1000+"");
////            Log.d(TAG,"currentFile...name..."+FileUtils.currentFile.getName());
////            String[] maxStr = FileUtils.currentFile.getName().split("_");
////            inputElectricity.setText(maxStr[1]);
////            inputVoltage.setText(maxStr[2]);
////            inputCoulomb.setText(maxStr[3]);
//            initChart(chart_electricity,electricityValues,"E");
//            initChart(chart_voltage,voltageValues,"V");
//            initChart(chart_coulomb,coulombValues,"C");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    private void setData(LineChart mChart,ArrayList<Entry> values,String label) {
//
//        LineDataSet set1;
//
//        if (mChart.getData() != null &&
//                mChart.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//
//            mChart.getData().notifyDataChanged();
//            mChart.notifyDataSetChanged();
//        }
//        else {
//            set1= new LineDataSet(values, label);
//            set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
//            set1.setCubicIntensity(0.2f);
//            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
//            set1.setDrawCircles(false);
//            set1.setLineWidth(2f);
//            set1.setHighLightColor(Color.rgb(244, 117, 117));
//            set1.setDrawValues(false);
//            if (label.equals("E")){
//                set1.setColor(Color.parseColor("#00a0e9"));
//            }else if (label.equals("C")){
//                set1.setColor(Color.parseColor("#981cbe"));
//            }else{
//                set1.setColor(Color.parseColor("#f8b551"));
//            }
//
//            // create a data object with the datasets
//            LineData data = new LineData(set1);
////            data.setValueTextColor(Color.WHITE);
////            data.setValueTextSize(9f);
//
//            // set data
//            mChart.setData(data);
//
//        }
//    }
//
//    private  Handler handler;
//    private final static int treatingCode = 1;
//    private final static int stopCode = 2;
//    private final static int receiveCode = 3;
//    private final static int stoppingCode = 4;
//    private final static int startCode = 5;
//    private final static int sendFailedCode = 6;
//
//    private class MyHandler extends Handler{
//        WeakReference<MainActivity> weakReference;
//
//        public MyHandler(MainActivity activity){
//            weakReference = new WeakReference<MainActivity>(activity);
//
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (weakReference.get() != null){
//                switch (msg.what){
//                    case treatingCode:
//                        inputElectricity.setEnabled(false);
//                        inputVoltage.setEnabled(false);
//                        inputCoulomb.setEnabled(false);
//                        inputElectricityRate.setEnabled(false);
//                        btn_switch.setText("停止");
//                        clear();
//                        break;
//                    case stopCode:
//                        isStart = false;
//                        btn_switch.setText("治疗");
//                        btn_switch.setEnabled(true);
//                        clear();
//                        break;
//                    case receiveCode:
//                        ReceiveVo vo = (ReceiveVo) msg.obj;
//                        tv_electricity.setText(((float)vo.electricity/100)+"");
//                        tv_voltage.setText(((float)vo.voltage/100)+"");
//                        tv_coulomb.setText(((float)vo.coulomb/1000)+"");
//                        addEntry(chart_electricity,"E",((float)vo.electricity/100));
//                        addEntry(chart_voltage,"V",((float)vo.voltage/100));
//                        progress_coulomb.setProgress(vo.coulomb/10);
//                        break;
//                    case stoppingCode:
//                        btn_switch.setText("停止中...");
//                        btn_switch.setEnabled(false);
//                        break;
//                    case startCode:
//                        //参数设置成功，可以开始治疗
//                        if (!isLocal){
//                            //发送设置参数
//                            isStart = true;
//                            mSerialPortPresenter.writeDataToSerial(Protocol.startCure(),null);
//                        }
//                        break;
//                    case sendFailedCode:
//                        Toast.makeText(MainActivity.this,"发送请求失败",Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }
//    }
//}
