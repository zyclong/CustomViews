package com.zyclong;


import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by zyclong on 2017/4/6.
 */

public class myService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new AlertDialog.Builder(myService.this).setTitle("提示")
                            .setCancelable(false).setMessage("亲，程序马上崩溃了...")
                            .setNeutralButton("没关系", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    //   AppManager.getAppManager().exitApp(activity);
                                    System.exit(0);//退出程序
                                    android.os.Process.killProcess(android.os.Process.myPid());//结束进程
                                }
                            }).create().show();
        return super.onStartCommand(intent, flags, startId);
    }


}
