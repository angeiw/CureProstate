package com.redcity.cureprostate.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.redcity.cureprostate.Protocol;
import com.redcity.cureprostate.mvp.presenter.SerialPortPresenter;
import com.redcity.cureprostate.vo.ParamVo;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.redcity.cureprostate.Protocol.UTRS_READ_CURE_PARAMETER;
import static com.redcity.cureprostate.Protocol.UTRS_WRITE_CURE_PARAMETER;
import static com.redcity.cureprostate.Protocol.UTRS_WRITE_END_CURE;
import static com.redcity.cureprostate.Protocol.UTRS_WRITE_START_CURE;

/**
 * Created by COREIGHT-101 on 2018/8/27.
 */

public class SendThread extends Thread {
    private  LinkedList<ParamVo> FIFOMessage = new LinkedList<>();;
    boolean isSend = false;
    private final String TAG = SendThread.class.getSimpleName();
    private Handler sendHandler;
    public Handler getSendHandler(){
        return sendHandler;
    }
    SerialPortPresenter serialPortPresenter;
    public SendThread(SerialPortPresenter serialPortPresenter){
        this.serialPortPresenter = serialPortPresenter;
    }
    int count;
    @Override
    public void run() {
        Looper.prepare();
        sendHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
//                Log.d(TAG,"sendHandler："+"msg..what..."+ (byte)msg.what+"....arg1..."+msg.arg1);
//                sendCommand(msg);
                if (msg.obj != null){//需要执行的指令存入缓存
                    ParamVo vo = (ParamVo) msg.obj;
                    sendCommand(vo);
                    isSend = true;
                    count = 0;
//                    FIFOMessage.add(vo);
//                    Log.i(TAG,"sendHandler："+"msg.....存入缓存"+FIFOMessage.get(0).order);
                }else {//接受线程一直发送的消息
                    if (isSend){
                        count++;
                    }
                }
//                else {//串口返回的数据
//                    if (FIFOMessage.size() > 0) {
//                        Log.w(TAG, "sendHandler：" + "msg.....验证...what..." + (byte) FIFOMessage.get(0).order + "....arg1..." + (byte) msg.what);
//                        if ((byte) FIFOMessage.get(0).order - (byte) msg.what == (byte) 0x80) {
//                            Log.d(TAG, "sendHandler：" + "msg.....验证成功可以再次发送");
//                            isSend = true;
//                            count = 0;
//                            FIFOMessage.remove(0);
//                        } else {
//                            Log.d(TAG, "sendHandler：" + "msg.....验证失败开始计数");
//                            count++;
//                            if (count == 10) {
//                                Log.d(TAG, "sendHandler：" + "msg....重复发送");
//                                sendCommand(FIFOMessage.get(0));
//                                count = 0;
//                            }
//                        }
//                    }
//                }
//                if (isSend && FIFOMessage.size() > 0){
//                    Log.d(TAG,"sendHandler："+"msg....发送指令");
//                    sendCommand(FIFOMessage.get(0));
////                    sendCommand(msg);
//                    isSend = false;
//                }

            }
        };



        serialPortPresenter.sendThreadOK();
        Looper.loop();



    }

    private void sendCommand(ParamVo paramVo){
        switch (paramVo.order){
            case UTRS_WRITE_START_CURE:
                serialPortPresenter.writeDataToSerial(Protocol.startCure(),null);
                break;
            case UTRS_WRITE_END_CURE:
                serialPortPresenter.writeDataToSerial(Protocol.stopCure(),null);
                break;
            case UTRS_WRITE_CURE_PARAMETER:
                serialPortPresenter.writeDataToSerial(Protocol.setParam(paramVo.electricityParamer,paramVo.voltageParamer,paramVo.coulombParamer,paramVo.electricityRateParamer),null);
                break;
            case UTRS_READ_CURE_PARAMETER:
                serialPortPresenter.writeDataToSerial(Protocol.getParam(),null);
                break;
        }
    }
}
