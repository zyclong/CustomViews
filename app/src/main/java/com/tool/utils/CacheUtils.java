package com.tool.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by zyclong-pc on 2017/3/13.
 */

public class CacheUtils {

    private static SharedPreferences mSp;
//    config_sp.xml 文件  存放位置 ：/data/data/<packageName>/shared_prefes/configureSp.xml
    private static final String CONFIGURE_FILE="configureSp";

    private static SharedPreferences getSharedFerences(Context context){
        if(mSp==null){
            mSp=context.getSharedPreferences(CONFIGURE_FILE,Context.MODE_PRIVATE);
        }
        return mSp;
    }

    /**
     * 按指定模式的到 SharedPreferences，mSp被重新赋值
     */
    public static SharedPreferences getSharedFerences(Context context,int mode){
//        if(mSp==null){
            mSp=context.getSharedPreferences(CONFIGURE_FILE,mode);
//        }
        return mSp;
    }

    /**
     *  保存布尔数据
     */
    public static void putBoolean(Context context,String key, boolean value) {
        SharedPreferences sp=getSharedFerences(context);
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     *   取布尔数据 ,返回的默认值是false
     */
    public static boolean getBoolean(Context context,String key) {
        SharedPreferences sp=getSharedFerences(context);
        return sp.getBoolean(key, false);
    }

    /**
     *   取布尔数据 ,返回的默认值是defvalue
     */
    public static boolean getBoolean(Context context,String key,boolean defvalue) {
        SharedPreferences sp=getSharedFerences(context);
        return sp.getBoolean(key, defvalue);
    }

    /**
     *   保存字符串
     */
    public static void putString(Context context,String key, String value) {
        SharedPreferences sp=getSharedFerences(context);
        sp.edit().putString(key, value).commit();
    }

    /**
     *   取字符串数据 ,默认返回的是 null
     */
    public static String getString(Context context,String key) {
        SharedPreferences sp=getSharedFerences(context);
        return sp.getString(key, null);
    }

    /**
     *    取字符串数据 ,默认返回的是defvalue
     */
    public static String getString(Context context,String key,String defvalue) {
        SharedPreferences sp=getSharedFerences(context);
        return sp.getString(key, defvalue);
    }

  //  TextUtils

}
