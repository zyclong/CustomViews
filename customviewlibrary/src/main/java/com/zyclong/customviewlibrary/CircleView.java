package com.zyclong.customviewlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zyclong-pc on 2017/2/24.
 */

public class CircleView extends View{

        private static final String TAG = "CircleView";
    private static final String NAMESPACE = "http://schemas.android.com/apk/com.zyclong.customviewlibrary";
    private static final String DEFAULT_COLOR ="#3304352d" ;
    private static final String DEFAULT_TEXT = "0.00 %";
    private static final String DEFAULT_NOTE_TEXT = "note";


    private int mMaxProgress = 100;

    private  int mCircleLineStrokeWidth = 4; //圆圈的粗细
    private  int mTxtStrokeWidth = 4;  //字体的粗细

        // 画圆所在的距形区域
        private final RectF mRectF;

        private final Paint mPaint;

        private final Context mContext;

        private String mTxtHint1="mTxtHint1";

        private String mTxtHint2="mTxtHint2";
    private String mText;
    private String mNoteText;
    private int mRadius,mColor;
    private int mAlpha=100;

    public CircleView(Context context, AttributeSet attrs) {
            super(context, attrs);
            mContext = context;
            mRectF = new RectF();
            mPaint = new Paint();
            init(attrs);
        }

    private void init(AttributeSet attrs) {
        String mColorStr;
       mColorStr =attrs.getAttributeValue(NAMESPACE,"Color");
        if(null== mColorStr){
            mColorStr =DEFAULT_COLOR;
        }
        mColor=Color.parseColor(mColorStr);


       mText=attrs.getAttributeValue(NAMESPACE,"text");
        if(null== mText){
            mText =DEFAULT_TEXT;
        }
       mNoteText=attrs.getAttributeValue(NAMESPACE,"noteText");
        if(null== mNoteText){
            mNoteText =DEFAULT_NOTE_TEXT;
        }
        mTxtHint2=mNoteText;
        mCircleLineStrokeWidth=attrs.getAttributeIntValue(NAMESPACE,"CircleLineStrokeWidth",mCircleLineStrokeWidth);
        mTxtStrokeWidth=attrs.getAttributeIntValue(NAMESPACE,"TxtStrokeWidth",mTxtStrokeWidth);
    }

    @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int width = this.getWidth();
            int height = this.getHeight();

            if (width != height) {
                int min = Math.min(width, height);
                width = min;
                height = min;
            }

            // 设置画笔相关属性
            mPaint.setAntiAlias(true);
            mPaint.setColor(mColor);
            canvas.drawColor(Color.TRANSPARENT);
            mPaint.setStrokeWidth(mCircleLineStrokeWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            // 位置
            mRectF.left = mCircleLineStrokeWidth / 2; // 左上角x
            mRectF.top = mCircleLineStrokeWidth / 2; // 左上角y
            mRectF.right = width - mCircleLineStrokeWidth / 2; // 左下角x
            mRectF.bottom = height - mCircleLineStrokeWidth / 2; // 右下角y
            canvas.drawArc(mRectF, 0,  360, false, mPaint);
            mPaint.setColor(mColor);
            mPaint.setAlpha(mAlpha);
            mPaint.setStyle(Paint.Style.FILL);
            //mPaint.setShadowLayer();

         //mPaint.setShadowLayer(15f,10f,10f,Color.GRAY);
        mPaint.setPathEffect(new DashPathEffect(new float[] { 8, 10, 8, 10}, 0));
        canvas.drawArc(mRectF, 0, 180, false, mPaint);



            // 绘制进度文案显示
            mPaint.setStrokeWidth(mTxtStrokeWidth);
            mPaint.setColor(mColor);
            String text = mText;
            int textHeight = height / 4;
            mPaint.setTextSize(textHeight);
            int textWidth = (int) mPaint.measureText(text, 0, text.length());
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, width / 2 - textWidth / 2, height / 2 + textHeight / 2, mPaint);

            if (!TextUtils.isEmpty(mTxtHint1)) {
                mPaint.setStrokeWidth(mTxtStrokeWidth);
                text = mTxtHint1;
                textHeight = height / 8;
                mPaint.setTextSize(textHeight);
                mPaint.setColor(Color.rgb(0x99, 0x99, 0x99));
                textWidth = (int) mPaint.measureText(text, 0, text.length());
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawText(text, width / 2 - textWidth / 2, height / 4 + textHeight / 2, mPaint);
            }

            if (!TextUtils.isEmpty(mTxtHint2)) {
                mPaint.setStrokeWidth(mTxtStrokeWidth);
                text = mTxtHint2;
                textHeight = height / 8;
                mPaint.setTextSize(textHeight);
                textWidth = (int) mPaint.measureText(text, 0, text.length());
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawText(text, width / 2 - textWidth / 2, 3 * height / 4 + textHeight / 2, mPaint);
            }
        }

        public int getMaxProgress() {
            return mMaxProgress;
        }

        public void setMaxProgress(int maxProgress) {
            this.mMaxProgress = maxProgress;
        }

        public void setProgress(int progress) {
         //   this.mProgress = progress;
            this.invalidate();
        }

        public void setProgressNotInUiThread(int progress) {
          //  this.mProgress = progress;
            this.postInvalidate();
        }

        public String getmTxtHint1() {
            return mTxtHint1;
        }

        public void setmTxtHint1(String mTxtHint1) {
            this.mTxtHint1 = mTxtHint1;
        }

        public String getmTxtHint2() {
            return mTxtHint2;
        }

        public void setmTxtHint2(String mTxtHint2) {
            this.mTxtHint2 = mTxtHint2;
        }

    public int getBrighterColor(int color){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv); // convert to hsv

        hsv[1] = hsv[1] - 1f; // less saturation
        hsv[2] = hsv[2] + 1f; // more brightness
        int darkerColor = Color.HSVToColor(hsv);
        return  darkerColor ;
    }
    }

