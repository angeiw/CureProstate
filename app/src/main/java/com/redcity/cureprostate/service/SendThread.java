package com.redcity.cureprostate.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.redcity.cureprostate.Protocol;
import com.redcity.cureprostate.mvp.presenter.SerialPortPresenter;
import com.redcity.cureprostate.util.ByteUtils;
import com.redcity.cureprostate.vo.ParamVo;

import static com.redcity.cureprostate.Protocol.UTRS_READ_CURE_PARAMETER;
import static com.redcity.cureprostate.Protocol.UTRS_WRITE_CURE_PARAMETER;
import static com.redcity.cureprostate.Protocol.UTRS_WRITE_END_CURE;
import static com.redcity.cureprostate.Protocol.UTRS_WRITE_START_CURE;

/**
 * Created by COREIGHT-101 on 2018/8/27.
 */

public class SendThread extends Thread {

    private final String TAG = SendThread.class.getSimpleName();
    private Handler handler;
    public Handler getHandler(){
        return handler;
    }
    SerialPortPresenter serialPortPresenter;
    public SendThread(SerialPortPresenter serialPortPresenter){
        this.serialPortPresenter = serialPortPresenter;
    }
    @Override
    public void run() {
        Looper.prepare();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                   Log.d(TAG,"收到消息了："+"msg....."+ msg.toString());
                    sendCommand(msg);


            }
        };
        Looper.loop();


    }

    private void sendCommand(Message msg){
        switch (msg.what){
            case UTRS_WRITE_START_CURE:
                serialPortPresenter.writeDataToSerial(Protocol.startCure(),null);
                break;
            case UTRS_WRITE_END_CURE:
                serialPortPresenter.writeDataToSerial(Protocol.stopCure(),null);
                break;
            case UTRS_WRITE_CURE_PARAMETER:
                ParamVo vo = (ParamVo) msg.obj;
                serialPortPresenter.writeDataToSerial(Protocol.setParam(vo.electricityParamer,vo.voltageParamer,vo.coulombParamer,vo.electricityRateParamer),null);
                break;
            case UTRS_READ_CURE_PARAMETER:
                serialPortPresenter.writeDataToSerial(Protocol.getParam(),null);
                break;
        }
    }
}
