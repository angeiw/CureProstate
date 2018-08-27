package com.redcity.cureprostate.mvp.view;

/**
 * Created by COREIGHT-101 on 2018/4/26.
 */

public interface ISerialPort {

    void noSupport();
    void noConnected();
    void noMoreDevices();
    void attached();
    void noPermission();
    void chipNoSupport();
    void connected();
    void sendFailed();
    void writeParameter();
    void readParameter(float V,float E,float C,float ER);
    void startCure();
    void receive(short voltage,short electricity,int coulomb,short time);
    void stopCure();
    void finishCure();

}
