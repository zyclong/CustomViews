package com.abellstarlite.wedgit.zyclong.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by zyclong on 2017/3/23.
 */

public class ImageUtils {

    public  static Bitmap RoundBitmap(Resources res, int resId,
                                      int reqWidth){
        Bitmap b=decodeSampledBitmapFromDrawable(res,resId,reqWidth,reqWidth);
        b=zoomImg(b,reqWidth,reqWidth);
        return getCroppedBitmap(b,reqWidth);
    }

    public  static Bitmap zoomBitmap(Resources res, int resId,
                                      int reqWidth){
        Bitmap b=decodeSampledBitmapFromDrawable(res,resId,reqWidth,reqWidth);
        b=zoomImg(b,reqWidth,reqWidth);
        return b;
    }

    public  static Bitmap RoundBitmap(BitmapDrawable bitmapDrawable,
                                      int reqWidth){
      //   b=decodeSampledBitmapFromDrawable(res,resId,reqWidth,reqWidth);
        Bitmap  b=zoomImg(bitmapDrawable.getBitmap(),reqWidth,reqWidth);
        return getCroppedBitmap(b,reqWidth);
    }

    public  static Bitmap RoundBitmap(Bitmap bitmap,
                                      int reqWidth){
        //   b=decodeSampledBitmapFromDrawable(res,resId,reqWidth,reqWidth);
        bitmap=zoomImg(bitmap,reqWidth,reqWidth);
        return getCroppedBitmap(bitmap,reqWidth);
    }

    public static Bitmap decodeSampledBitmapFromDrawable(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     *  处理图片
     * @param bm 所要转换的bitmap
     * @param newWidth 新的宽
     * @param newHeight 新的高
     * @return 指定宽高的bitmap
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片   www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {

        Bitmap sbmp;

        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        } else {
            sbmp = bmp;
        }

        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f, sbmp.getHeight() / 2 + 0.7f,
                sbmp.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }


}
