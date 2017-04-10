package com.zyclong.customviewlibrary.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by zyclong on 2017/3/18.
 */

public class ProgressBarLinelayout extends LinearLayout {

    private int mPadding=0;/*四边的padding最小值*/
    private int mStrokeCircleBG=25;/*背景圆的宽度*/
    private  float mPaddingPrecent=0;
    private int mRaidus;/*背景圆半径，其他圆的半径取与它的相对值*/

    private float childPrecent=0.6f;
    private String TAG="ProgressBarLinelayout";

    public ProgressBarLinelayout(Context context) {
        super(context);
    }

    public ProgressBarLinelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressBarLinelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean isChildrenDrawnWithCacheEnabled() {
        return super.isChildrenDrawnWithCacheEnabled();
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        int  padding= (int) (mRaidus*(1-0.6)/2);
//        Log.i(TAG, "onLayout: padding"+padding);
//        super.onLayout(changed,l+padding,t+padding,r-padding,b-padding);

        super.onLayout(changed,l,t,r,b);
    }

    /**
     * 测量
     * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        // measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=width>getMeasuredWidth() ? getMeasuredWidth() : width;
        height=height>getMeasuredHeight() ? getMeasuredHeight() : height;
        setMeasuredDimension(width,height);
        width = getWidth();
        height = getHeight();
        width  = width < height  ? width : height;
        mRaidus=width/2-mPadding-mStrokeCircleBG;
        mPadding= (int) (mPaddingPrecent*width);
    }
}
