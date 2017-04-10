package com.abellstarlite.wedgit.zyclong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

import com.zyclong.customview.R;


/**
 * Created by zyclong on 2017/3/16.
 */

public class SeekBarWithNumber extends SeekBar {

    private static final String TAG = "SeekBarWithNumber";
    private float mTextSize;
    private int mTextColor = Color.BLACK;
    private Paint mPaint;
    private int mBGDrawableID = R.drawable.diaper_bar_remind_setting;/*背景id*/
    private int mBTNDrawableID = R.drawable.diaper_btn_remind_setting;/*滑块id*/
    private BitmapDrawable mBGDrawable;
    private BitmapDrawable mBTNDrawable;
    private Paint mPaintLine;
    private float mStrokeWidth = 0.25f;
    private String mLoneColor = "#4DDCEA";
    private boolean mIsDrawText = true;
    private double mAverage = 100;
    Rect mBGDrawableRect;/*绘图区域*/
    private OnSeekBarChangeListener mOnSeekBarChangeListener;
    private float mTextSizePrecent = 0.3f;
    private Rect BGRect;
    private int mInterval;


    public SeekBarWithNumber(Context context) {
        this(context, null);
    }

    public SeekBarWithNumber(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekBarWithNumber(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }

    private void init(AttributeSet attrs) {

        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        mPaint.setAntiAlias(true);

        mPaintLine = new Paint();

      //  mPaintLine.setStrokeCap(Paint.Cap.ROUND);
        mPaintLine.setColor(Color.parseColor(mLoneColor));
        mBGDrawableRect = new Rect();

        BGRect = new Rect();
        mBGDrawable = (BitmapDrawable) getResources().getDrawable(mBGDrawableID);
        mBTNDrawable = (BitmapDrawable) getResources().getDrawable(mBTNDrawableID);


    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getHeight();
        width = Math.max(mBGDrawable.getIntrinsicWidth() + getPaddingLeft() + getPaddingRight() + mBTNDrawable.getIntrinsicHeight(), width);
        height = Math.max(mBTNDrawable.getIntrinsicHeight() + getPaddingBottom() + getPaddingTop(), height);

        setMeasuredDimension(width, height);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mBGDrawable = (BitmapDrawable) getResources().getDrawable(mBGDrawableID);
        mBTNDrawable = (BitmapDrawable) getResources().getDrawable(mBTNDrawableID);

        mBGDrawableRect.left = getPaddingLeft();
        mBGDrawableRect.top = getPaddingTop();
        mBGDrawableRect.right = getWidth() - getPaddingRight();
        mBGDrawableRect.bottom = getHeight() - getPaddingBottom();

        float drawableheight = mBGDrawable.getIntrinsicHeight();
        float drawableheightBTN = mBTNDrawable.getIntrinsicHeight();
        float drawablewidth = mBGDrawable.getIntrinsicWidth();
        float drawablewidthBTN = mBTNDrawable.getIntrinsicWidth();
        mTextSize = mTextSizePrecent * drawablewidthBTN;

        Rect BTNRect = new Rect();
        float offset;
        mPaintLine.setStrokeWidth(mStrokeWidth * drawableheight);

        BGRect.left = (int) (mBGDrawableRect.centerX() - drawablewidth / 2 + drawableheightBTN / 2);
        BGRect.top = (int) (mBGDrawableRect.centerY() - drawableheight / 2);
        BGRect.right = (int) (mBGDrawableRect.centerX() + drawablewidth / 2 - drawableheightBTN / 2);
        BGRect.bottom = (int) (mBGDrawableRect.centerY() + drawableheight / 2);

        if ((BGRect.width()) > mBGDrawableRect.width()) {
            BGRect.left = (int) (mBGDrawableRect.left + drawableheightBTN / 2);
            BGRect.right = (int) (mBGDrawableRect.right - drawableheightBTN / 2);
        }

        if ((BGRect.height()) > mBGDrawableRect.height()) {
            BGRect.top = mBGDrawableRect.top;
            BGRect.bottom = mBGDrawableRect.bottom;
        }

        offset = getProgress() * (BGRect.right - BGRect.left) / getMax();

        BTNRect.left = (int) (BGRect.left + offset - drawablewidthBTN / 2);
        BTNRect.top = (int) (mBGDrawableRect.centerY() - drawableheightBTN / 2);
        BTNRect.right = (int) (BTNRect.left + drawablewidthBTN);
        BTNRect.bottom = (int) (BTNRect.top + drawableheightBTN);

        if ((BTNRect.width()) > mBGDrawableRect.width()) {
            BTNRect.left = mBGDrawableRect.left;
            BTNRect.right = mBGDrawableRect.right;
        }

        if ((BTNRect.height()) > mBGDrawableRect.height()) {
            BTNRect.top = mBGDrawableRect.top;
            BTNRect.bottom = mBGDrawableRect.bottom;
        }

        mBTNDrawable.setBounds(BTNRect);
        mBGDrawable.setBounds(BGRect);
        mBGDrawable.draw(canvas);
        //
        if (offset != 0) {/*华为EMUI api 6.0系统在画长度等于0的线时，Cap.ROUND 画出的效果很难看，绘制一条斜线*/
            canvas.drawLine((float) (BGRect.left + 0.02 * drawablewidth), BTNRect.centerY(),
                    (float) (BGRect.left + 0.02 * drawablewidth) + offset, BTNRect.centerY(), mPaintLine);
        }
        mBTNDrawable.draw(canvas);

        if (mIsDrawText) {
            mPaint.setTextSize(mTextSize);
            String str = getProgress() + "%";
            Rect textB = new Rect();
            mPaint.getTextBounds(str, 0, str.length(), textB);
            canvas.drawText(str,
                    BTNRect.centerX() - textB.width() / 2,
                    BTNRect.centerY() + textB.height() / 2,
                    mPaint
            );
        }

    }

//    private void GCDrawable(BitmapDrawable mDrawable) {
//
//        mDrawable.setCallback(null);
//        Bitmap bitmap = mDrawable.getBitmap();
//        if(bitmap != null && !bitmap.isRecycled()){
//            bitmap.recycle();
//            bitmap = null;
//        }
//        System.gc();
//    }


//    public void setmBTNDrawableID(@DrawableRes int BTNDDrawableID ){
//        this.mBTNDrawableID=BTNDDrawableID;
//        invalidate();
//    }
//
//    public void setmBGDrawableID(@DrawableRes int BGDDrawableID ){
//        this.mBGDrawableID=BGDDrawableID;
//        invalidate();
//    }


    /**
     * 设置seekbar的样式
     *
     * @param BTNDrawableID 背景图
     * @param BGDrawableID  滑块图标
     * @param average       均分数，把seekbar等分成average指定的份数
     * @param isDrawText    是否绘制文本
     */
    public void setDrawableIDs(@DrawableRes int BTNDrawableID, @DrawableRes int BGDrawableID, int average, boolean isDrawText) {
        this.mBGDrawableID = BGDrawableID;
        this.mBTNDrawableID = BTNDrawableID;
        mIsDrawText = isDrawText;
        mAverage = average;
        setMax(average);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mOnSeekBarChangeListener != null) {
                    mOnSeekBarChangeListener.onStartTrackingTouch(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                setProgress(getNewProgress(event.getX()));
                break;
            case MotionEvent.ACTION_UP:
                setProgress(getNewProgress(event.getX()));
                if (mOnSeekBarChangeListener != null) {
                    mOnSeekBarChangeListener.onStopTrackingTouch(this);
                }
                break;
        }
        return true;
    }


    private int getNewProgress(float x) {
        int realwidth;
        float offset;
        // mBGDrawable= (BitmapDrawable) getResources().getDrawable(mBGDrawableID);
        realwidth = BGRect.width();
        offset = x - BGRect.left;

        if (offset <= 0) {
            return 0;
        }
        if (offset >= realwidth) {
            return (int) mAverage;
        }
        float interval = (float) (realwidth / mAverage);
        return Math.round(offset / interval);
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        mOnSeekBarChangeListener = l;
        super.setOnSeekBarChangeListener(l);
    }

//    @Override
//    public synchronized int getProgress() {
//        int progress=super.getProgress();
//        if(progress%mInterval!=0){
//            int p=progress%mInterval;
//            int i=progress/mInterval;
//            if(p>=mInterval/2){
//                progress=(i+1)*mInterval;
//            }else{
//                progress=i*mInterval;
//            }
//        }
//        setProgress(progress);
//        return progress;
//    }


    @Override
    public synchronized void setProgress(int progress) {
        if (mInterval == 0) {
            mInterval = 1;
        }
        if (progress % mInterval != 0) {
            int p = progress % mInterval;
            int i = progress / mInterval;
            if (p >= mInterval / 2) {
                progress = (i + 1) * mInterval;
            } else {
                progress = i * mInterval;
            }
        }
        super.setProgress(progress);
    }

    /**
     * 设置最小间隔,getProgerss()返回该间隔的倍数，默认为1
     *
     * @param interval 最小间隔范围为（0~getMax()）
     */
    public void setMinInvertal(int interval) {
        if (interval <= 0 || interval > getMax()) {
            return;
        }
        mInterval = interval;
    }
}
