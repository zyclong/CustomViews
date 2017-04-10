package com.zyclong.customviewlibrary.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.graphics.Paint;

/**
 * Created by zyclong on 2017/3/15.
 */

public class LineProgressBar extends ProgressBar {
    private final Paint mPaintStoke;
    private  Paint mPaint;
    private float paddingLeftPrecent=0.1f;/*相对于控件宽度*/
    private float paddingTopPrecent=0.2f;/*相对于控件高度*/
    private float progerssBarPrecent=0.8f;/*相对于控件宽度*/
    private float mStrokeWidth;
    private float mStrokeWidthPrecent=0.02f;/*相对于屏幕高度*/
    DisplayMetrics mDisplayMetrics;
    private int mColor= Color.GRAY;
    private int mColorStoke=Color.WHITE;

    public LineProgressBar(Context context) {
        this(context,null);
    }

    public LineProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LineProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Display mDisplay = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        mDisplayMetrics = new DisplayMetrics();
        mDisplay.getMetrics(mDisplayMetrics);
        mStrokeWidth=mDisplayMetrics.heightPixels*mStrokeWidthPrecent;
        mPaint=new Paint();
        mPaintStoke=new Paint();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       int width= getWidth();
        int height=getHeight();
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(mColor);


        canvas.drawLine(width*paddingLeftPrecent,
                height*paddingTopPrecent+mStrokeWidth/2,
                width*(progerssBarPrecent+paddingLeftPrecent),
                height*paddingTopPrecent+mStrokeWidth/2,
                mPaint);

      //  mPaintStoke.setStrokeWidth(mStrokeWidth);
        mPaintStoke.setStrokeWidth((float) (mStrokeWidth*0.2));
        mPaintStoke.setStyle(Paint.Style.STROKE);
        mPaintStoke.setStrokeJoin(Paint.Join.ROUND);
        mPaintStoke.setColor(mColorStoke);

        canvas.drawLine(width*paddingLeftPrecent,
                height*paddingTopPrecent,
                width*(progerssBarPrecent+paddingLeftPrecent),
                height*paddingTopPrecent,
                mPaintStoke);
        canvas.drawLine(width*paddingLeftPrecent,
                height*paddingTopPrecent+mStrokeWidth,
                width*(progerssBarPrecent+paddingLeftPrecent),
                height*paddingTopPrecent+mStrokeWidth,
                mPaintStoke);

        float x=width*paddingLeftPrecent;
        float y=  height*paddingTopPrecent+mStrokeWidth/2;
        RectF oval = new RectF();/*确定绘图区域*/
        oval.left = x-mStrokeWidth/2;
        oval.top = y-mStrokeWidth/2;
        oval.right =x+mStrokeWidth/2;
        oval.bottom =y+mStrokeWidth/2;
        canvas.drawArc(oval,90,180,false,mPaintStoke);
        oval.left = x-mStrokeWidth/2+ width*progerssBarPrecent;
        oval.top = y-mStrokeWidth/2;
        oval.right =x+mStrokeWidth/2+ width*progerssBarPrecent;
        oval.bottom =y+mStrokeWidth/2;
        canvas.drawArc(oval,-90,180,false,mPaintStoke);
    }
}
