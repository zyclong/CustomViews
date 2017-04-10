package com.zyclong.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by zyclong on 2017/4/7.
 */

public class AppUtils {

    private static Boolean isDebug = null;

    public static boolean isDebug() {
        return isDebug == null ? false : isDebug.booleanValue();
    }

    public static void syncIsDebug(Context context) {
        if (isDebug == null) {
            isDebug = context.getApplicationInfo() != null &&
                    (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
    }
}
