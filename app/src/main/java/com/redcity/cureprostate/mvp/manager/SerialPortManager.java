package com.redcity.cureprostate.mvp.manager;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.util.Log;


import com.redcity.cureprostate.util.ByteUtils;
import com.redcity.cureprostate.util.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;

import tw.com.prolific.driver.pl2303.PL2303Driver;

/**
 * Created by COREIGHT-101 on 2018/4/24.
 */

public class SerialPortManager {
    private final String TAG = SerialPortManager.class.getSimpleName();

    private Context mContext;
    private final String ACTION_USB_PERMISSION = "com.prolific.pl2303hxdsimpletest.USB_PERMISSION";
    private PL2303Driver mSerial;
    private File localFile;
    public SerialPortManager(Context context){
        mContext = context;
        mSerial = new PL2303Driver((UsbManager) mContext.getSystemService(Context.USB_SERVICE), mContext, ACTION_USB_PERMISSION);
    }

    public boolean checkUSB(){

        if (!mSerial.PL2303USBFeatureSupported()) {
            mSerial = null;
            return false;
        }
        return true;
    }

    public boolean checkConnected(){
        if(!mSerial.isConnected()) {
            if( !mSerial.enumerate() ) {
                return false;
            } else {

            }
        }//if isConnected
        return true;
    }

    public int openUsbSerial() {
        Log.d(TAG, "Enter  openUsbSerial");
        if(null==mSerial)
            return -1;

        if (mSerial.isConnected()) {
            if (!mSerial.InitByBaudRate(PL2303Driver.BaudRate.B9600,700)) {
                if(!mSerial.PL2303Device_IsHasPermission()) {
                    return 1;
                }

                if(mSerial.PL2303Device_IsHasPermission() && (!mSerial.PL2303Device_IsSupportChip())) {
                    return 2;
                }
            } else {

                mSerial.setDTR (true);
                mSerial.setRTS (true);
                return 0;

            }
        }//isConnected
        Log.d(TAG, "Leave openUsbSerial");
        return 3;

    }

    byte[] rxBuffer = null;
    byte[] retBuffer = new byte[64];
    int position = 0;
    int count=0;
    public byte[] readDataFromSerial() {

        int len;
        // byte[] rbuf = new byte[4096];
        byte[] rbuf = new byte[64];
//        StringBuffer sbHex=new StringBuffer();

//        Log.d(TAG, "Enter readDataFromSerial");

//        if(null==mSerial)
//            return null;
//
//        if(!mSerial.isConnected())
//            return null;

        len = mSerial.read(rbuf);
//        if(len<0) {
//            Log.d(TAG, "Fail to bulkTransfer(read data)");
//            return null;
//        }

        if (len > 0) {
//            Log.d(TAG,"readDataFromSerial....len..."+len);
//            Log.d(TAG,"readDataFromSerial....rbuf..."+Arrays.toString(rbuf));
            if (rxBuffer == null){
                rxBuffer = new byte[64];
                position = 0;
            }
            count = 0;
            System.arraycopy(rbuf,0,rxBuffer,position,len);
            position += len;

//            return rbuf;
        }
        else {
            count++;
            if (count > 30 && rxBuffer != null){
                retBuffer = rxBuffer;
                rxBuffer = null;
                Log.d(TAG,"readDataFromSerial....position..."+position);
                Log.d(TAG,"readDataFromSerial....rbuf..."+Arrays.toString(retBuffer));
                return retBuffer;
            }

        }
            return null;

    }//readDataFromSerial


    public boolean writeDataToSerial(byte[] data) {

        Log.d(TAG, "Enter writeDataToSerial");

        if(null==mSerial)
            return false;

        if(!mSerial.isConnected())
            return false;

//        String strWrite = etWrite.getText().toString();
		/*
        //strWrite="012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
       // strWrite = changeLinefeedcode(strWrite);
         strWrite="012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
         if (SHOW_DEBUG) {
            Log.d(TAG, "PL2303Driver Write(" + strWrite.length() + ") : " + strWrite);
        }
        int res = mSerial.write(strWrite.getBytes(), strWrite.length());
		if( res<0 ) {
			Log.d(TAG, "setup: fail to controlTransfer: "+ res);
			return;
		}

		Toast.makeText(this, "Write length: "+strWrite.length()+" bytes", Toast.LENGTH_SHORT).show();
		 */
        // test data: 600 byte
        //strWrite="AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
//        if (SHOW_DEBUG) {
//            Log.d(TAG, "PL2303Driver Write 2(" + strWrite.length() + ") : " + strWrite);
//        }
        int res = mSerial.write(data, data.length);
        if( res<0 ) {
            Log.d(TAG, "setup2: fail to controlTransfer: "+ res);
            return false;
        }

//        Toast.makeText(this, "Write length: "+strWrite.length()+" bytes", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "Leave writeDataToSerial");
        return true;
    }//writeDataToSerial


    public void initCacheFile(){
        FileUtils.initCacheFile(mContext);
    }

    public byte[] readDataFromLocal(int off) {
        return FileUtils.readToSDCard(off);

    }//readDataFromLocal

    public void writeDataToLocal(byte[] data){

        FileUtils.writeToSDCard(data);


    }//writeDataToLocal

    /**
     * 循环读取文件，获取数据
     */
    public JSONArray readFile() throws JSONException {
        boolean isRead = true;
        JSONArray fileArray = new JSONArray();
        long le = FileUtils.currentFile.length();
        int i = 0;
        while(le - (i*20) != 0){
            byte[] data = FileUtils.readToSDCard(i);
            Log.d(TAG,"readLocalThread...."+ Arrays.toString(data));
            if (data[0] == -6){
                if (data[3] == 5 && data[4] == 11){//接收数据
                    if (data[5] == 15 || data[5] == 16){
                        isRead = false;
                    }
                    short voltage = ByteUtils.byte2Short(ByteUtils.getBytes(data,6,7));
                    short electricity = ByteUtils.byte2Short(ByteUtils.getBytes(data,8,9));
                    int coulomb = ByteUtils.bytes2Int(ByteUtils.getBytes(data,10,13));
                    short time = ByteUtils.byte2Short(ByteUtils.getBytes(data,14,15));
                    JSONObject jsonData = new JSONObject();
                    jsonData.put("voltage",voltage);
                    jsonData.put("electricity",electricity);
                    jsonData.put("coulomb",coulomb);
                    jsonData.put("time",time);
                    fileArray.put(jsonData);
                }
            }
            i++;
        }
//        while(isRead){
////            Log.d(TAG,"readFile...i..."+i);
//            byte[] data = FileUtils.readToSDCard(i);//20条数据
//            Log.d(TAG,"readFile...data..."+Arrays.toString(data));
////            Log.d(TAG,"readFile..."+ Arrays.toString(data));
//            if (data[0] == -6){
//                if (data[3] == 5 && data[4] == 11){//接收数据
//                    if (data[5] == 15 || data[5] == 16){
//                        isRead = false;
//                    }
//                    short voltage = ByteUtils.byte2Short(ByteUtils.getBytes(data,6,7));
//                    short electricity = ByteUtils.byte2Short(ByteUtils.getBytes(data,8,9));
//                    int coulomb = ByteUtils.bytes2Int(ByteUtils.getBytes(data,10,13));
//                    short time = ByteUtils.byte2Short(ByteUtils.getBytes(data,14,15));
//                    JSONObject jsonData = new JSONObject();
//                    jsonData.put("voltage",voltage);
//                    jsonData.put("electricity",electricity);
//                    jsonData.put("coulomb",coulomb);
//                    jsonData.put("time",time);
//                    fileArray.put(jsonData);
//                }
//            }
//            i++;
//        }
        return fileArray;
    }


    public void endSerial(){
        if(mSerial!=null) {
            mSerial.end();
            mSerial = null;
        }
    }
}
