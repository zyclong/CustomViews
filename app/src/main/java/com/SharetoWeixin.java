package com;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zyclong-pc on 2017/3/6.
 */

public class SharetoWeixin {
    //private String s=""


    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    /**
     * 分享信息到朋友
     *
     * @param file,假如图片的路径为path，那么file = new File(path);
     */
    public static void shareToFriend(Activity activity,File file) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(componentName);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_TEXT, "测试微信");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        activity.startActivity(intent);
    }

    // 保存到sdcard
    public static void savePic(Bitmap b) {
       String strFileName= Environment.getExternalStorageDirectory().getAbsolutePath()+"/savePic.jpeg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享信息到朋友圈
     *
     * @param file，假如图片的路径为path，那么file = new File(path);
     */
    public static void shareToTimeLine(Activity activity,File file) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(componentName);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//   intent.setAction(android.content.Intent.ACTION_SEND_MULTIPLE);
//   ArrayList<Uri> uris = new ArrayList<Uri>();
//   for (int i = 0; i < images.size(); i++) {
//     Uri data = Uri.fromFile(new File(thumbPaths.get(i)));
//     uris.add(data);
//   }
//   intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.setType("image/*");
      activity.startActivity(intent);
    }
}
