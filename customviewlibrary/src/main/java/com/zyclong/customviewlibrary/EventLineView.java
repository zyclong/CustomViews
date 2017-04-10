package com.zyclong.customviewlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import static android.content.ContentValues.TAG;

/**线性事件控件
 * Created by zyclong-pc on 2017/2/28.
 */

public class EventLineView extends View {

    private int mInterval =12;
    private int mIntervalFrame =2;
    private int mLineLenngth=2000;
    private int mLineColor= Color.GRAY;
    private Drawable mBackground;
    private int mColor;
    private int mBGColor=Color.WHITE;
    private float mRadiusFill=20;

    private int mMinWidth=320;
    private int mLineNumber=5;
    private int mOffset=100;
    private int mTextOffset=20;
    private int mFillCircleColor=Color.RED;
    private String mText="时间";

    //  private CustomeCursor cursor;

    public EventLineView(Context context) {
        this(context,null);
    }

    public EventLineView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EventLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams();
        init(attrs);

    }

    private void initParams() {




    }

    private void init(AttributeSet attrs) {
       // TypedArray ta=getContext().obtainStyledAttributes(attrs,)
       // mBackground=getBackground();
        //mColor=getSolidColor();
        //Log.i(TAG, "init:mBackground "+mBackground.toString());
        //Log.i(TAG, "init:mColor "+mColor);
       // setBackground(getResources().getDrawable(Color.WHITE));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth;
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.AT_MOST://如果widthSize是当前视图可使用的最大宽度
                measureWidth = getPaddingLeft() + getPaddingRight();
                break;
            case MeasureSpec.EXACTLY://如果widthSize是当前视图可使用的绝对宽度
                measureWidth = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.UNSPECIFIED://如果widthSize对当前视图宽度的计算没有任何参考意义
                measureWidth = getPaddingLeft() + getPaddingRight();
                break;
            default:
                measureWidth = getPaddingLeft() + getPaddingRight();
                break;
        }

        //计算高度
        int measureHeight;
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.AT_MOST://如果heightSize是当前视图可使用的最大宽度
                measureHeight = getPaddingTop() + getPaddingBottom();
                break;
            case MeasureSpec.EXACTLY://如果heightSize是当前视图可使用的绝对宽度
                measureHeight = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.UNSPECIFIED://如果heightSize对当前视图宽度的计算没有任何参考意义
                measureHeight = getPaddingTop() + getPaddingBottom();
                break;
            default:
                measureHeight =  getPaddingTop() + getPaddingBottom();
                break;
        }
        measureHeight+=getMinHeight();

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(measureWidth < getMeasuredWidth()){
            measureWidth = getMeasuredWidth();
        }

        if(measureHeight < getMeasuredHeight()){
            measureHeight = getMeasuredHeight();
        }

        setMeasuredDimension(measureWidth, measureHeight);
        Log.i(TAG, "onMeasure: "+getWidth()+"dddd"+getHeight());
    }

    private int getMinHeight() {
        return mLineLenngth;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

       // this.getApplicationWindowToken().
        Log.i(TAG, "onDraw: "+canvas.getWidth());
        Log.i(TAG, "onDraw: "+canvas.getHeight());
        Paint bg=new Paint();
        bg.setColor(mBGColor);
        canvas.drawRect(0,0,getWidth(),getHeight(),bg);

        int width=getWidth();
        Log.i(TAG, "onDraw: "+(width-getPaddingLeft()-getPaddingRight()));
        int interval= mInterval *(width-getPaddingLeft()-getPaddingRight())/(mInterval + mIntervalFrame)/mLineNumber;
        int intervalFrame= mIntervalFrame *(width-getPaddingLeft()-getPaddingRight())/(mInterval + mIntervalFrame)/2;
        Log.i(TAG, "onDraw:interval "+interval);
        Log.i(TAG, "onDraw:intervalFrame "+intervalFrame);
        Paint paint=new Paint();
        paint.setColor(mLineColor);
        int startX=getPaddingLeft()+intervalFrame,startY=getPaddingTop();
        int stopX=startX,stopY=mLineLenngth+startY;
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        for(int i=mLineNumber;i>0;i--) {
            Log.i(TAG, "onDraw:startX+=interval "+startX);
            startX+=interval;
            stopX+=interval;
            canvas.drawLine(startX, startY,stopX , stopY, paint);
        }
        startX=getPaddingLeft()+intervalFrame;
        startY=getPaddingTop();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mFillCircleColor);
        for(int i=mOffset;i<mLineLenngth;i=i+mOffset){
            canvas.drawCircle(startX,startY+i,mRadiusFill,paint);
            canvas.drawText(mText,startX+mTextOffset,startY+i,paint);
            Log.i(TAG, "mLineLenngth onDraw: "+i);
        }

        canvas.restore();


    }

    class d extends LinearLayout{

        public d(Context context) {
            super(context);
        }

        public d(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public d(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }

//        @Override
//        protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
//            return super.drawChild(canvas, child, drawingTime);
//        }


    }

}
