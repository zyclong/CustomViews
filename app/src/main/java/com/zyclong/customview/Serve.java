package com.zyclong.customview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.WindowManager;

/**
 * Created by zyclong on 2017/3/17.
 */

public class Serve extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      //
        //
//         ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).
        return super.onStartCommand(intent, flags, startId);
    }
}
