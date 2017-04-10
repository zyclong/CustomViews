package com.abellstarlite.wedgit.zyclong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by zyclong-pc on 2017/3/3.
 * 没有自定义属性
 */

public class DataDayTextView extends View {
    String TAG="DataDayTextView";

    public DataDayView.DataDayTextViewInfo mInfo;
    private int baseY;
    private int mInterval;
    private int mIntervalFrame;
    private int mLineNumber;
    private Paint p;
    private float mTextSize;
    private int mColor;
    private int mBaseWidth;
    private int mBaseHeight;

    public DataDayTextView(Context context) {
            this(context, null);
        }

    public DataDayTextView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

    public DataDayTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
      //  AdaptationParams();

    }

    private void init(AttributeSet attrs) {
        mTextSize=12;/*dp*/
        baseY=8;/*dp*/
        baseY=dip2px(baseY);
        mTextSize=dip2px(mTextSize);
        mColor=Color.WHITE;
        mInterval=15;
        mIntervalFrame=2;
        mLineNumber=5;
        p=new Paint();
        p.setTextSize(mTextSize);
        p.setColor(mColor);
        mInfo=new DataDayView.DataDayTextViewInfo("2000-01-01",mInterval,mIntervalFrame,mLineNumber);
    }

    public  int dip2px(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;// 设备密度
        int px = (int) (dip * density + 0.5f);// 3.1->3, 3.9+0.5->4.4->4
        return px;
    }


    public void setmInfo(DataDayView.DataDayTextViewInfo mInfo) {
        try{
        this.mInfo = mInfo;
        mLineNumber=mInfo.info[0];
        mInterval=mInfo.info[1];
        mIntervalFrame=mInfo.info[2];
        invalidate();
        }catch (Exception e){
            init(null);
            Log.e(TAG, "setmInfo: ",e );
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getMeasuredWidth(), (int) (p.getTextSize()+baseY*2));

    }

    @Override
    protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        int interval = mInterval * (getWidth() - getPaddingLeft() - getPaddingRight()) / (mInterval + mIntervalFrame) / mLineNumber;
        int intervalFrame = mIntervalFrame * (getWidth() - getPaddingLeft() - getPaddingRight()) / (mInterval + mIntervalFrame) / 2;
        p.setTextSize(mTextSize);
        Log.i(TAG, "onDraw: mTextSize= "+mTextSize);

        float x=0,y=0;
        /*居中显示文本*/
        for(int i=0;i<mInfo.time.length;i++){
            x= intervalFrame+interval*i-p.measureText(mInfo.time[i])/2;
            if(x<=0){
                x= intervalFrame+interval*i;
            }
            y=getHeight()-baseY;
            canvas.drawText(mInfo.time[i],x,y,p);
        }

    }

}
