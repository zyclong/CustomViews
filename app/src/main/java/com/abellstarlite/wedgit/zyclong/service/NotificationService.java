package com.abellstarlite.wedgit.zyclong.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zyclong.customview.Main6Activity;
import com.zyclong.customview.R;


/**
 * Created by zyclong on 2017/3/27.
 */

public class NotificationService extends Service {
    static final String TAG = "NotificationService";
    private NotificationManager notificationManager;
    NotificationUtils n;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        n = new NotificationUtils();
        n.create(this);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       String title= intent.getStringExtra(NSinfo.TITIL);
        String content=intent.getStringExtra(NSinfo.CONTENT);
        String tricker=intent.getStringExtra(NSinfo.TICKER);
//        intent.getStringExtra(NSinfo.TITIL);
        try {
            n.sendOnGoing(title,content,tricker);
            n.sendOne(title,content,tricker);
            Log.i(TAG, "onStartCommand: send");
        } catch (Exception e) {
            Log.e(TAG, "onStartCommand: ", e);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public class NotificationUtils {
        Context context;
        Notification.Builder builderAlways;
        Notification.Builder builderOne;

        void create(Context context) {
            this.context=context;
            notificationManager = (NotificationManager) context
                    .getSystemService(NOTIFICATION_SERVICE);
            builderAlways = new Notification.Builder(context);
            builderOne = new Notification.Builder(context);
            builderAlways.setSmallIcon(R.drawable.ic_mail_outline_white_24dp);// 设置图标
            builderAlways.setWhen(System.currentTimeMillis());// 设置通知来到的时间
            builderAlways.setOngoing(true);

        }

        void sendOnGoing(String title,String content,String tricker) {
            Intent intentTo = new Intent(context, Main6Activity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, intentTo,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builderAlways.setContentIntent(pendingIntent);
            builderAlways.setContentTitle(title);// 设置通知的标题
            builderAlways.setContentText(content);// 设置通知的内容
            builderAlways.setTicker(tricker);// 状态栏上显示
            Notification notification = builderAlways.build();
            notificationManager.notify(0, notification);
        }

        void sendOne(String title,String content,String tricker) {
            Intent intentTo = new Intent(context, Main6Activity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 1, intentTo,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builderOne.setContentIntent(pendingIntent);
            builderOne.setContentTitle(title);// 设置通知的标题
            builderOne.setContentText(content);// 设置通知的内容
            builderOne.setTicker(tricker);// 状态栏上显示
            builderAlways.setOngoing(false);//可取消通知，不在Ongoing区域
            Notification notification = builderAlways.build();
            notificationManager.notify(1, notification);
        }

    }

    public static class NSinfo{
        public static String TITIL="TITIL";
        public static String CONTENT="CONTENT";
        public static String TICKER="Ticker";
    }
}
