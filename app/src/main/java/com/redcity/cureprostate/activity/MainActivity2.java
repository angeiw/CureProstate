package com.redcity.cureprostate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.redcity.cureprostate.R;

/**
 * Created by COREIGHT-101 on 2018/6/22.
 */

public class MainActivity2 extends Activity {

    private final String TAG = MainActivity2.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main2);


//        DisplayMetrics mDisplayMetrics = new DisplayMetrics();//屏幕分辨率容器
//        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
//        int width = mDisplayMetrics.widthPixels;
//        int height = mDisplayMetrics.heightPixels;
//        float density = mDisplayMetrics.density;
//        int densityDpi = mDisplayMetrics.densityDpi;
//        Log.d(TAG,"Screen Ratio: ["+width+"x"+height+"],density="+density+",densityDpi="+densityDpi);
//        Log.d(TAG,"Screen mDisplayMetrics: "+mDisplayMetrics);
//
//
//        Display mDisplay = getWindowManager().getDefaultDisplay();
//        int width1 = mDisplay.getWidth();
//        int height1 = mDisplay.getHeight();
//        Log.d(TAG,"Screen Default Ratio: ["+width1+"x"+height1+"]");
//        Log.d(TAG,"Screen mDisplay: "+mDisplay);



        DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();
        int width = mDisplayMetrics.widthPixels;
        int height = mDisplayMetrics.heightPixels;
        float density = mDisplayMetrics.density;
        int densityDpi = mDisplayMetrics.densityDpi;
        Log.d(TAG,"Screen Ratio: ["+width+"x"+height+"],density="+density+",densityDpi="+densityDpi);
        Log.d(TAG,"Screen mDisplayMetrics: "+mDisplayMetrics);

    }
}
