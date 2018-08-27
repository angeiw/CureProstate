package com.redcity.cureprostate.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.redcity.cureprostate.Protocol;
import com.redcity.cureprostate.mvp.presenter.SerialPortPresenter;
import com.redcity.cureprostate.mvp.view.ISerialPort;
import com.redcity.cureprostate.util.Constant;
import com.redcity.cureprostate.util.FileUtils;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by COREIGHT-101 on 2018/8/20.
 */

public class SerialPortService extends Service implements ISerialPort {

    private final String TAG = SerialPortService.class.getSimpleName();

    private SerialPortPresenter mSerialPortPresenter;
    private MyBinder binder = new MyBinder();
    ReceiveThread receiveThread;
    SendThread sendThread;
    ProcessThread processThread;
    @Override
    public void onCreate() {
        super.onCreate();
        mSerialPortPresenter = new SerialPortPresenter(this,this);
        mSerialPortPresenter.checkUSB();
        mSerialPortPresenter.checkConnected();


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class MyBinder extends Binder implements IService{

        @Override
        public boolean verifyParameter(short electricityParamer, short voltageParamer, int coulombParamer, short electricityRateParamer) {
            if (electricityParamer > 12000){
                Toast.makeText(SerialPortService.this,"电流最大值过大",Toast.LENGTH_SHORT).show();
                return false;
            }else if (voltageParamer > 1200) {
                Toast.makeText(SerialPortService.this,"电压最大值过大",Toast.LENGTH_SHORT).show();
                return false;
            }else if (coulombParamer > 1000000) {
                Toast.makeText(SerialPortService.this,"库伦最大值过大",Toast.LENGTH_SHORT).show();
                return false;
            }else if (electricityRateParamer > 10000) {
                Toast.makeText(SerialPortService.this,"电流变化率过大",Toast.LENGTH_SHORT).show();
                return false;
            }else {
                return true;
            }
        }


        @Override
        public void sendData(byte order,Object o) {
            Log.d(TAG,"sendData...."+ order);
            if (o == null){
                sendThread.getHandler().sendEmptyMessage(order);
            }else {
                Message msg = Message.obtain();
                msg.what = order;
                msg.obj = o;
                sendThread.getHandler().sendMessage(msg);
            }
//            mSerialPortPresenter.writeDataToSerial(data,null);
//            final Timer timer = new Timer();
//            TimerTask task = new TimerTask() {
//                @Override
//                public void run() {
//                    mSerialPortPresenter.writeDataToSerial(data, new SerialPortPresenter.receiveListern() {
//                        @Override
//                        public void returnCommd(byte b) {
//                            Log.d(TAG,"data...."+Protocol.byteToInt(data[3])+".....b....."+Protocol.byteToInt(b));
//                            if (Protocol.byteToInt(data[3]) - Protocol.byteToInt(b) == 128){
//                                timer.cancel();
//
//                            }
//                        }
//                    });
//                }
//            };
//            timer.schedule(task, 300, 300);
        }
    }


    @Override
    public void noSupport() {
        Toast.makeText(this, "No Support USB host API", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void noConnected() {
        Toast.makeText(this, "no connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void noMoreDevices() {
        Toast.makeText(this, "no more devices found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void attached() {
        Toast.makeText(this, "attached", Toast.LENGTH_SHORT).show();
        mSerialPortPresenter.openUsbSerial();
    }

    @Override
    public void noPermission() {
        Toast.makeText(this, "cannot open, maybe no permission", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void chipNoSupport() {
        Toast.makeText(this, "cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void connected() {
        Toast.makeText(this, "connected : " , Toast.LENGTH_SHORT).show();
        if (receiveThread != null && receiveThread.isAlive()){
            receiveThread.interrupt();
            receiveThread = null;
        }
        receiveThread = new ReceiveThread();
        receiveThread.start();

        if (sendThread != null && sendThread.isAlive()){
            sendThread.interrupt();
            sendThread = null;
        }
        sendThread = new SendThread(mSerialPortPresenter);
        sendThread.start();

//        if (processThread != null && processThread.isAlive()){
//            processThread.interrupt();
//            processThread = null;
//        }
//        processThread = new ProcessThread();
//        processThread.start();
    }

    @Override
    public void sendFailed() {
    }

    @Override
    public void writeParameter() {
        Intent intent = new Intent(Constant.writeParameterAction);
        sendBroadcast(intent);
    }

    @Override
    public void readParameter(float V,float E,float C,float ER) {
        Intent intent = new Intent(Constant.readParameterAction);
        intent.putExtra("V",V);
        intent.putExtra("E",E);
        intent.putExtra("C",C);
        intent.putExtra("ER",ER);
        sendBroadcast(intent);
    }

    @Override
    public void startCure() {
        FileUtils.initCacheFile(this);//
        Intent intent = new Intent(Constant.startCureAction);
        sendBroadcast(intent);
    }

    @Override
    public void receive(short voltage, short electricity, int coulomb, short time) {
        Intent intent = new Intent(Constant.receiveDataAction);
        intent.putExtra("voltage",((float)voltage/100));
        intent.putExtra("electricity",((float)electricity/100));
        intent.putExtra("coulomb",((float)coulomb/1000));
        intent.putExtra("coulombProgress",(coulomb/100));
        Log.d(TAG,"receive...coulombProgress..."+coulomb);
        intent.putExtra("time",time);
        sendBroadcast(intent);
    }

    @Override
    public void stopCure() {
        Intent intent = new Intent(Constant.stopCureAction);
        sendBroadcast(intent);
    }

    @Override
    public void finishCure() {
        Intent intent = new Intent(Constant.finishCureAction);
        sendBroadcast(intent);
    }

    class ReceiveThread extends Thread{
        @Override
        public void run() {
            super.run();
            mSerialPortPresenter.receiveData(true);
        }
    }

    class ProcessThread extends Thread{
        @Override
        public void run() {
            super.run();
            mSerialPortPresenter.processData();
        }
    }
}
