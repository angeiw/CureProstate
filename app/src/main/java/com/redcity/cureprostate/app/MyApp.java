package com.redcity.cureprostate.app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.redcity.cureprostate.service.IService;
import com.redcity.cureprostate.service.SerialPortService;

/**
 * Created by COREIGHT-101 on 2018/8/20.
 */

public class MyApp extends Application {

    IService iService;
    @Override
    public void onCreate() {
        super.onCreate();

//        startService(new Intent(this, SerialPortService.class));

//        Intent intent = new Intent(this, SerialPortService.class);
//        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }

//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            iService = (IService) iBinder;
//            iService.receiveData();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//
//        }
//    };
}
