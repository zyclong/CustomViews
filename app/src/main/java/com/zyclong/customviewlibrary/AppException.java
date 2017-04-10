package com.zyclong.customviewlibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by zyclong on 2017/4/5.
 */

public class AppException extends Exception implements Thread.UncaughtExceptionHandler {

    private static final long serialVersionUID = -6262909398048670705L;
    private static Context APPcontext;

    private String message;

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private AppException() {
        super();
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public AppException(String message, Exception excp) {
        super(message, excp);
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取APP异常崩溃处理对象
     *
     * @param context
     * @return
     */
    public static AppException getAppExceptionHandler(Context context) {
       APPcontext=context;
        return new AppException();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handleException(ex) && mDefaultHandler != null) {
         //   mDefaultHandler.uncaughtException(thread, ex);
        }

    }

    /**
     * 自定义异常处理
     *
     * @param ex
     * @return true:处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new AlertDialog.Builder(APPcontext).setTitle("提示")
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


       // final Activity activity = AppManager.getAppManager().currentActivity();

//        if (activity == null) {
//            return false;
//        }

        new Thread() {
            @Override
            public void run() {
                try {
                      Looper.prepare();
                    Toast.makeText(APPcontext, "程序要崩了", Toast.LENGTH_SHORT).show();
//
                       Looper.loop();
                } catch (Exception r) {
                    Log.e("Exception", "run: ", r);
                }
            }
        }.start();

        return true;
    }

}
