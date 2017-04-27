package com.zyclong.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.anye.greendao.gen.AppWifiInfoDao;
import com.anye.greendao.gen.DaoMaster;
import com.anye.greendao.gen.DaoSession;
import com.qihoo.linker.logcollector.LogCollector;
import com.qihoo.linker.logcollector.upload.HttpParameters;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by zyclong on 2017/3/31.
 */

public class App extends Application {
    public static App app;
    private static final String UPLOAD_URL = "http://xxxxxxxx";

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private final Class<? extends org.greenrobot.greendao.AbstractDao<?, ?>>[]
            allDBDao = new Class[]{
            AppWifiInfoDao.class
    };


    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.syncIsDebug(this);
        //对意外闪退做处理
//        if (!BuildConfig.DEBUG)
//        AppUtils
        //this.getApplicationInfo().

        CrashReport.initCrashReport(getApplicationContext(), "70eae21bb1", AppUtils.isDebug());
        HttpParameters params = new HttpParameters();
        params.add("key1", "value1");
        params.add("key2", "value2");
        params.add("key3", "value3");
        app=this;
       // boolean isDebug = true;
     //  data/data/pn/log/log.xx
        //set debug mode , you can see debug log , and also you can get logfile in sdcard;
        // LogCollector.upload(boolean isWifiOnly);
//       boolean isWifiOnly; true代表只在wifi情况下发送，false代表有网的情况下就发送（包括流量和wifi）
//        您可以在service，activity等位置的合适时机触发，不会卡界面也不会影响性能。
        LogCollector.setDebugMode(AppUtils.isDebug());
        LogCollector.init(getApplicationContext(), UPLOAD_URL, params);//params can be null
        setDatabase();
//
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {

        mHelper = new DaoMaster.DevOpenHelper(this, "IPCAMERA.db", null) {
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
              //  MigrationHelper.migrate(db, allDBDao);
                //   super.onUpgrade(db, oldVersion, newVersion);
                GreenDaoUpdateUtils.updateTable(db,allDBDao);
            }
        };

        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        db = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

}
