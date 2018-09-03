package com.redcity.cureprostate.mvp.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.redcity.cureprostate.util.ByteUtils;
import com.redcity.cureprostate.mvp.manager.SerialPortManager;
import com.redcity.cureprostate.mvp.view.ISerialPort;
import com.redcity.cureprostate.util.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by COREIGHT-101 on 2018/4/26.
 */

public class SerialPortPresenter {

    private final String TAG = SerialPortPresenter.class.getSimpleName();

    private ISerialPort iSerialPort;
    private SerialPortManager manager;
    private LinkedList<byte[]> FIFOData = new LinkedList<>();
    public SerialPortPresenter(Context context,ISerialPort iSerialPort){
        manager = new SerialPortManager(context);
        this.iSerialPort = iSerialPort;
    }

    public void sendThreadOK(){
        Log.d(TAG,"sendThread already");
        iSerialPort.sendThread();
    }


    // check USB host function.
    public void checkUSB(){
        if (!manager.checkUSB()){
            iSerialPort.noSupport();
        }
    }

    public void checkConnected(){
        if (manager.checkConnected()){
            iSerialPort.attached();
        }else{
            iSerialPort.noMoreDevices();
        }
    }

    public void openUsbSerial(){
        switch (manager.openUsbSerial()){
            case -1:
                return;
            case 0://成功
                //创建文件
//                manager.initCacheFile();
//                打开接受循环
                iSerialPort.connected();
                break;
            case 1:
                iSerialPort.noPermission();
                break;
            case 2:
                iSerialPort.chipNoSupport();
                break;
            case 3:
                iSerialPort.noConnected();
                break;
            default:
                break;
        }
    }

    /**
     * 从本地接受数据
     */
    public void readDataFromLocal(){
        off = 0;
//        manager.initCacheFile();
//        iSerialPort.connected(receiveData(false));
    }
    byte[] writeData;
    byte[] data;
    int off = 0;
    int count;
    receiveListern receiveListern;
    public  void receiveData(Handler sendHandler) {

                while (true){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                     data = manager.readDataFromSerial();
                     Log.w(TAG,Arrays.toString(data));
                    if (data != null){//分析接受到的数据
//                        if (receiveListern != null)
//                            receiveListern.returnCommd(data[3]);
                        Log.w(TAG,"data != null....."+Arrays.toString(data));
//                        Log.d(TAG,"msg.....order..."+order);
                        processData(data);
//                        Message message = new Message();
//                        message.what = 0;
//                        message.arg1 = data[3];
//                        sendHandler.sendMessage(message);
                        sendHandler.sendEmptyMessage(data[3]);
//                        FIFOData.add(data);
                    }
                    else{//数据是空
                        sendHandler.sendEmptyMessage((byte) 0x80);
                    }
                }

//        Looper.prepare();
//        receiveHandler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                Log.d(TAG,"receiveHandler："+"msg....."+ msg.what);
//
//
//            }
//        };
//        Looper.loop();
//                if (isSerial){
//                    data = manager.readDataFromSerial();
//                    Log.d(TAG,"串口接收数据..."+ Arrays.toString(data));
//                    if (data != null) {
//                        manager.writeDataToLocal(data);
//                    }
//                }else{//从本地文件读取
//                    data = manager.readDataFromLocal(off);
//                    Log.d(TAG,"本地缓存数据..."+ Arrays.toString(data));
//                    off++;
//                }

//            }
//        };

//        return runnable;
    }


    public void processData(){
        while (true){
//            Log.d(TAG,"processData....."+FIFOData.toString());
            if (FIFOData.size() > 0 && FIFOData.get(0) != null){
                processData(FIFOData.get(0));
                FIFOData.remove(0);
            }
        }
    }

    private void processData(byte[] data){
        Log.d(TAG,"processData....."+Arrays.toString(data));
        if (data[0] == (byte)0xFA){
            if (data[3] == (byte)0x03 && data[4] == (byte)0x00){//开始治疗
//                                manager.initCacheFile();
                iSerialPort.startCure();//正在治疗
            }else if (data[3] == (byte)0x05 && data[4] == (byte)0x0B){//接收数据
                if (data[5] == 15 || data[5] == 16){
                    finishCure();
                    return;
                }
                short voltage = ByteUtils.byte2Short(ByteUtils.getBytes(data,6,7));
                short electricity = ByteUtils.byte2Short(ByteUtils.getBytes(data,8,9));
                int coulomb = ByteUtils.bytes2Int(ByteUtils.getBytes(data,10,13));
                short time = ByteUtils.byte2Short(ByteUtils.getBytes(data,14,15));
                Log.d(TAG,"voltage..."+voltage+"...electricity..."+electricity+"....coulomb...."+coulomb+"....time..."+time);
                iSerialPort.receive(voltage,electricity,coulomb,time);
                                byte[] dst = new byte[20];
                               System.arraycopy(data,0,dst,0,20);
                                manager.writeDataToLocal(dst);
            }else if (data[3] == (byte)0x04 && data[4] == (byte)0x00){//停止
                iSerialPort.stopCure();
            }else if (data[3] == (byte)0x06 && data[4] == (byte)0x00){//设置参数成功，可以开始治疗
                Log.d(TAG,"writeParameter...."+Arrays.toString(data));
                iSerialPort.writeParameter();
            }else if (data[3] == (byte)0x07 && data[4] == (byte)0x0A){//读取参数
                Log.d(TAG,"readParameter..V.."+ByteUtils.byte2Short(ByteUtils.getBytes(data,5,6))+"...E.."+ByteUtils.byte2Short(ByteUtils.getBytes(data,7,8))+"....C..."+ByteUtils.bytes2Int(ByteUtils.getBytes(data,9,12))+"....ER..."+ByteUtils.byte2Short(ByteUtils.getBytes(data,13,14)));
                iSerialPort.readParameter(((float)ByteUtils.byte2Short(ByteUtils.getBytes(data,5,6))/100), ((float)ByteUtils.byte2Short(ByteUtils.getBytes(data,7,8))/100),((float)ByteUtils.bytes2Int(ByteUtils.getBytes(data,9,12))/1000), ((float)ByteUtils.byte2Short(ByteUtils.getBytes(data,13,14))/1000));
            }
        }
    }

     public void writeDataToSerial(byte[] data,receiveListern receiveListern){
//         manager.writeDataToSerial(data);\\\\\\\\\
           if (manager.writeDataToSerial(data)){
               Log.d(TAG,"writeDataToSerial........");
               this.receiveListern = receiveListern;
           }else {
               iSerialPort.sendFailed();
           }
    }

   public interface receiveListern{
       void returnCommd(byte b);
    }

    public void endSerial(){
       manager.endSerial();
    }

    public JSONArray readFile() throws JSONException {
        return manager.readFile();
    }

    public byte[] readFileTest(int off){
        return manager.readDataFromLocal(off);
    }

    public void finishCure(){
        iSerialPort.finishCure();
    }

}
