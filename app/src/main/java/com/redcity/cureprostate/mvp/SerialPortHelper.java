//package com.redcity.cureprostate.mvp;
//
//import android.content.Context;
//import android.hardware.usb.UsbManager;
//
//import tw.com.prolific.driver.pl2303.PL2303Driver;
//
///**
// * Created by COREIGHT-101 on 2018/4/24.
// * 串口帮助类
// */
//
//public class SerialPortHelper {
//
//    private Context mContext;
//    private static SerialPortHelper instance = null;
//    private final String ACTION_USB_PERMISSION = "com.prolific.pl2303hxdsimpletest.USB_PERMISSION";
//    private PL2303Driver mSerial;
//
//    public static SerialPortHelper getInstance(Context context){
//        if (instance == null){
//            instance = new SerialPortHelper(context);
//        }
//        return instance;
//    }
//    private SerialPortHelper(Context context){
//        mContext = context;
//    }
//
//    public  PL2303Driver getSerial(){
//        if (mSerial == null){
//            mSerial = new PL2303Driver((UsbManager) mContext.getSystemService(Context.USB_SERVICE), mContext, ACTION_USB_PERMISSION);
//        }
//        return mSerial;
//    }
//
//
//
//
//
//
//
//
//}
