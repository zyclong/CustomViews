package com.zyclong.app;

import android.app.Application;

import com.qihoo.linker.logcollector.LogCollector;
import com.qihoo.linker.logcollector.upload.HttpParameters;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by zyclong on 2017/3/31.
 */

public class App extends Application {
    App app;
    private static final String UPLOAD_URL = "http://xxxxxxxx";

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.syncIsDebug(this);
        //对意外闪退做处理
//        if (!BuildConfig.DEBUG)
//        AppUtils
        //this.getApplicationInfo().

        CrashReport.initCrashReport(getApplicationContext(), "35f7414cae", AppUtils.isDebug());
        HttpParameters params = new HttpParameters();
        params.add("key1", "value1");
        params.add("key2", "value2");
        params.add("key3", "value3");
      //  app=this;
       // boolean isDebug = true;
     //  data/data/pn/log/log.xx
        //set debug mode , you can see debug log , and also you can get logfile in sdcard;
        // LogCollector.upload(boolean isWifiOnly);
//       boolean isWifiOnly; true代表只在wifi情况下发送，false代表有网的情况下就发送（包括流量和wifi）
//        您可以在service，activity等位置的合适时机触发，不会卡界面也不会影响性能。
        LogCollector.setDebugMode(AppUtils.isDebug());
        LogCollector.init(getApplicationContext(), UPLOAD_URL, params);//params can be null
//
    }



}
