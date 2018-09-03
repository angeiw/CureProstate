package com.redcity.cureprostate.service;

import android.util.Log;

import com.redcity.cureprostate.util.ByteUtils;
import com.redcity.cureprostate.util.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by COREIGHT-101 on 2018/8/29.
 */

public class ReadLocalThread extends Thread {
    private final String TAG = ReadLocalThread.class.getSimpleName();
    private IReadLocalCallBack callBack;
    public ReadLocalThread(IReadLocalCallBack callBack){
        this.callBack = callBack;
    }

    @Override
    public void run() {
        JSONArray fileArray = new JSONArray();
       long le = FileUtils.currentFile.length();
        int i = 0;
       while(le - (i*20) != 0){
           byte[] data = FileUtils.readToSDCard(i);
           Log.d(TAG,"readLocalThread...."+ Arrays.toString(data));
           if (data[0] == -6){
               if (data[3] == 5 && data[4] == 11){//接收数据
                   short voltage = ByteUtils.byte2Short(ByteUtils.getBytes(data,6,7));
                   short electricity = ByteUtils.byte2Short(ByteUtils.getBytes(data,8,9));
                   int coulomb = ByteUtils.bytes2Int(ByteUtils.getBytes(data,10,13));
                   short time = ByteUtils.byte2Short(ByteUtils.getBytes(data,14,15));
                   JSONObject jsonData = new JSONObject();
                   try {
                       jsonData.put("voltage",voltage);
                        jsonData.put("electricity",electricity);
                         jsonData.put("coulomb",coulomb);
                        jsonData.put("time",time);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   fileArray.put(jsonData);
               }
           }
           i++;
       }

        callBack.callBack(fileArray);

    }
}
