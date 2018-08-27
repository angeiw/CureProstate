package com.redcity.cureprostate.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.redcity.cureprostate.activity.SerialPortActivity;


public class BootBroadcastReceiver extends BroadcastReceiver {
//   static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override  
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent mainActivityIntent = new Intent(context, SerialPortActivity.class);
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
            context.startActivity(mainActivityIntent);  
        }  
    }  
  
}  