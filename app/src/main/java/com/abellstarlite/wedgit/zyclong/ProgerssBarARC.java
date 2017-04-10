package com.abellstarlite.wedgit.zyclong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zyclong.customview.R;


/**
 * Created by zyclong-pc on 2017/3/6.
 * 手机横竖屏问题未解决？？？
 * 背景图片被改用在旋转图片
 * 参数的适配采用参照720x1080
 * <p>
 * 注释了虚线圆部分的代码
 * 注释了自定义属性相关代码
 */

public class ProgerssBarARC extends View {
    private static final String TAG = "ProgerssBarARC";
    private Runnable AnimRunner;

    /**
     * mPaintCircleBG 背景圆的画笔
     * mPaintCircleBordar 背景圆描边的画笔
     * mPaintCircleProgress 进度条画笔
     * mPaintCircleDotted 虚线圆的画笔
     * mPaintText 虚线圆的画笔
     */
    private Paint mPaintCircleBG, mPaintCircleBordar, mPaintCircleProgress, mPaintCircleDotted, mPaintText;
    private int mRaidus = 60;/*背景圆半径，其他圆的半径取与它的相对值dp*/
    private int mColorCircleBG = Color.parseColor("#53b9e6");/*背景圆的颜色*/
    private int mStrokeCircleBG = 9;/*背景圆的宽度 dp*/
    private float mStartPosition = 135f;/*进度条开始的角度*/
    private float mRadian = 270;/*进度条的弧度*/

    private String mTextColor = "#2FBCFC";
    private int mColorCircleBGBordar = Color.WHITE;/*背景圆的颜色*/
    private int mStrokeCircleBGBordar = 1;/*背景圆的宽度 dp*/
    private int mProgress = 0;/* 0 ~ 100,表示进度*/
    private float mTextSize = 1f;/*进度文本的TextSize，默认是控件宽度*mTextSize_mRaidus */
    private float mTextSize_mRaidus = 0.6f;/*调整进度文本的TextSize的比例*/
    private Matrix mMatrix = new Matrix();/*设置渐变属性*/
    /*进度条渐变色*/
    private int[] mColorsShader = new int[]{
            Color.parseColor("#52d292"),
//            Color.parseColor("#b9fb0f"),
            Color.YELLOW,
            Color.YELLOW
    };
    private Drawable mBackgroundDrawable;

    private Handler mHandler;
    private long mDelayMillis = 200;/*重绘的时间 ms*/
    private float mDegrees = 0;

    private boolean isAnim = true;
    int width;
    int height;
    Matrix matrix;
    long mLastTime;
    private float mDegreeInterval = 8;


    public ProgerssBarARC(Context context) {
        this(context, null);
    }

    public ProgerssBarARC(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgerssBarARC(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            if (null != attrs) {
                /*自定义属性*/
//                TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.ProgerssBarARC);
//                mPaddingPrecent=typedArray.getFraction(R.styleable.ProgerssBarARC_padding,1,1,0.1f);
//                mTextSize=typedArray.getFraction(R.styleable.ProgerssBarARC_progressTextSize,1,1,1f);
//                mProgress= (int) (typedArray.getFraction(R.styleable.ProgerssBarARC_progress,100,1,100f)+0.01);
//                mColorCircleBGBordar=typedArray.getColor(R.styleable.ProgerssBarARC_progerssBarARCColor,Color.WHITE);
//               // typedArray.getLayoutDimension()
//                typedArray.recycle();
            }

            // 如果手机版本在4.0以上,则开启硬件加速
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }

            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    //   Log.i(TAG, "handleMessage: invalidate(); ");
//                    mDegrees = rotation(mDegrees);
//                    mLastTime = System.currentTimeMillis();
//                    invalidate();
//                    if (isAnim) {
//                        mHandler.sendEmptyMessageDelayed(0, mDelayMillis);
//                    }
                }
            };
            // mHandler.sendEmptyMessageDelayed(0, mDelayMillis);
            mStrokeCircleBGBordar = dip2px(mStrokeCircleBGBordar);
            mStrokeCircleBG = dip2px(mStrokeCircleBG);
            mRaidus = dip2px(mRaidus);
            mTextSize = mRaidus * mTextSize_mRaidus;
            mLastTime = System.currentTimeMillis();
//            matrix = new Matrix();
            mBackgroundDrawable = getResources().getDrawable(R.drawable.diaper_icon_percent_of_usage_pion);

        } catch (Exception e) {
            Log.e(TAG, "ProgerssBarARC: ", e);
        }
    }

    /*重绘时，旋转的角度*/
    private float rotation(float Degrees) {
        Degrees += mDegreeInterval;
        return Degrees % 360;
    }


    public int dip2px(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;// 设备密度
        //Log.i(TAG, "dip2px: "+density);
        int px = (int) (dip * density + 0.5f);// 3.1->3, 3.9+0.5->4.4->4
        return px;
    }

    /**
     * 测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //计算宽度
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
                measureHeight = (mBackgroundDrawable != null? mBackgroundDrawable.getIntrinsicHeight():0) + getPaddingTop() + getPaddingBottom();
                break;
            case MeasureSpec.EXACTLY://如果heightSize是当前视图可使用的绝对宽度
                measureHeight = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.UNSPECIFIED://如果heightSize对当前视图宽度的计算没有任何参考意义
                measureHeight = (mBackgroundDrawable != null? mBackgroundDrawable.getIntrinsicHeight():0) + getPaddingTop() + getPaddingBottom();
                break;
            default:
                measureHeight = (mBackgroundDrawable != null? mBackgroundDrawable.getIntrinsicHeight():0) + getPaddingTop() + getPaddingBottom();
                break;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(measureWidth < getMeasuredWidth()){
            measureWidth = getMeasuredWidth();
        }

        if(measureHeight < getMeasuredHeight()){
            measureHeight = getMeasuredHeight();
        }

        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        try {
            /**
             * 处理动画抖动问题
             */
//            if (isAnim) {
//                int i = (int) ((System.currentTimeMillis() - mLastTime) / (mDelayMillis / mDegreeInterval));
//                i = (int) Math.min(i, mDegreeInterval);
//                mDegrees += i;
//                matrix.postRotate(mDegrees, getWidth() / 2, getHeight() / 2);//旋转矩阵
//            }

            mRaidus = Math.min(getWidth() / 2, getHeight() / 2);
            mRaidus = mRaidus - mStrokeCircleBG;
            canvas.save();
            //画背景圆
            mTextSize = mTextSize > mRaidus * mTextSize_mRaidus ? mRaidus * mTextSize_mRaidus : mTextSize;
            mPaintCircleBG = new Paint();
            mPaintCircleBG.setColor(mColorCircleBG);
            mPaintCircleBG.setStrokeWidth(mStrokeCircleBG);
            RectF oval = new RectF();/*确定绘图区域*/
            oval.left = getWidth() / 2 - mRaidus;
            oval.top = getHeight() / 2 - mRaidus;
            oval.right = getWidth() / 2 + mRaidus;
            oval.bottom = getHeight() / 2 + mRaidus;
            mPaintCircleBG.setStrokeCap(Paint.Cap.ROUND);
            mPaintCircleBG.setStyle(Paint.Style.STROKE);
            canvas.drawArc(oval, mStartPosition, mRadian, false, mPaintCircleBG);


            //画背景进度条圆
            mPaintCircleProgress = new Paint();
            mPaintCircleProgress.setAntiAlias(true);
            mPaintCircleProgress.setStrokeWidth(mStrokeCircleBG);
            mPaintCircleProgress.setStrokeCap(Paint.Cap.ROUND);
            mPaintCircleProgress.setStyle(Paint.Style.STROKE);
    /*渐变颜色旋转mStartPosition-10，包裹圆角*/
            mMatrix.setRotate(mStartPosition - 5, oval.centerX(), oval.centerY());

            SweepGradient shader = new SweepGradient(oval.centerX(), oval.centerY(),
                    mColorsShader, null
            );
            shader.setLocalMatrix(mMatrix);
            mPaintCircleProgress.setShader(shader);
            canvas.drawArc(oval, mStartPosition, mProgress / 100f * mRadian, false, mPaintCircleProgress);


            //画背景圆的bordar(一个封闭的环形，两个弧，两个半圆)
            mPaintCircleBordar = new Paint();
            mPaintCircleBordar.setAntiAlias(true);
            mPaintCircleBordar.setStrokeWidth((float) (mStrokeCircleBGBordar));
            mPaintCircleBordar.setColor(mColorCircleBGBordar);
            mPaintCircleBordar.setStrokeCap(Paint.Cap.ROUND);
            mPaintCircleBordar.setStyle(Paint.Style.STROKE);

            RectF ovalBordar = new RectF();

            ovalBordar.left = oval.left - mStrokeCircleBG / 2;
            ovalBordar.top = oval.top - mStrokeCircleBG / 2;
            ovalBordar.right = oval.right + mStrokeCircleBG / 2;
            ovalBordar.bottom = oval.bottom + mStrokeCircleBG / 2;
            canvas.drawArc(ovalBordar, mStartPosition, mRadian, false, mPaintCircleBordar);

            ovalBordar.left = oval.left + mStrokeCircleBG / 2;
            ovalBordar.top = oval.top + mStrokeCircleBG / 2;
            ovalBordar.right = oval.right - mStrokeCircleBG / 2;
            ovalBordar.bottom = oval.bottom - mStrokeCircleBG / 2;
            canvas.drawArc(ovalBordar, mStartPosition, mRadian, false, mPaintCircleBordar);

            float x, y;/*半圆的圆心坐标*/
            x = (float) (oval.centerX() + mRaidus * Math.cos(Math.toRadians(mStartPosition + mRadian)));
            y = (float) (oval.centerY() + mRaidus * Math.sin(Math.toRadians(mStartPosition + mRadian)));
            ovalBordar.left = x - mStrokeCircleBG / 2;
            ovalBordar.top = y - mStrokeCircleBG / 2;
            ovalBordar.right = x + mStrokeCircleBG / 2;
            ovalBordar.bottom = y + mStrokeCircleBG / 2;
            canvas.drawArc(ovalBordar, mStartPosition + mRadian, 180, false, mPaintCircleBordar);

            x = (float) (oval.centerX() + mRaidus * Math.cos(Math.toRadians(mStartPosition)));
            y = (float) (oval.centerY() + mRaidus * Math.sin(Math.toRadians(mStartPosition)));
            ovalBordar.left = x - mStrokeCircleBG / 2;
            ovalBordar.top = y - mStrokeCircleBG / 2;
            ovalBordar.right = x + mStrokeCircleBG / 2;
            ovalBordar.bottom = y + mStrokeCircleBG / 2;
            canvas.drawArc(ovalBordar, 180 + mStartPosition, 180, false, mPaintCircleBordar);

            Rect rect = new Rect();
            rect.left = (int) oval.left + mStrokeCircleBG;
            rect.right = (int) oval.right - mStrokeCircleBG;
            rect.top = (int) oval.top + mStrokeCircleBG;
            rect.bottom = (int) oval.bottom - mStrokeCircleBG;
//            if (mBackgroundDrawable == null) {
//                mBackgroundDrawable = getResources().getDrawable(R.drawable.diaper_icon_percent_of_usage_pion);
//                return;
//            }
//            mBackgroundDrawable.setBounds(rect);
//            canvas.setMatrix(matrix);
//            mBackgroundDrawable.draw(canvas);
//            matrix.reset();
//            canvas.setMatrix(null);


     /*画进度值文本*/
            mPaintText = new Paint();
            mPaintText.setColor(Color.parseColor(mTextColor));
            mPaintText.setTextSize(mTextSize);
            float f = mPaintText.measureText(mProgress + "%");
            canvas.drawText(mProgress + "%", oval.centerX() - f / 2, oval.centerY() + mPaintText.descent(), mPaintText);
            canvas.restore();

        } catch (Exception e) {
            Log.e(TAG, "onDraw: ", e);
        }

    }

    private float rotationTime(float Degrees) {
        Degrees += (System.currentTimeMillis() - mLastTime) * mDegreeInterval / mDelayMillis;
        Log.i(TAG, "rotationTime: " + (System.currentTimeMillis() - mLastTime));
        return Degrees;
    }


    /**
     * 劫持背景图片，把背景图作为y圆内动画的图片
     *
     * @param background
     */
    @Override
    public void setBackground(Drawable background) {
        mBackgroundDrawable = background;
        //  super.setBackground(background);
    }

    /**
     * 保证在主线程调用
     *
     * @param mProgress
     */
    public void setmProgress(int mProgress) {
        if (mProgress > 100) {
            mProgress = 100;
        }
        if (mProgress < 0) {
            mProgress = 0;
        }
        this.mProgress = mProgress;
        invalidate();
    }

    /**
     * 保证在非主线程调用
     *
     * @param mProgress
     */
    public void setmProgressNotUI(int mProgress) {
        if (mProgress > 100) {
            mProgress = 100;
        }
        if (mProgress < 0) {
            mProgress = 0;
        }
        this.mProgress = mProgress;
        postInvalidate();
    }

    /**
     * 旋转动画的开关,关闭此方法
     *
     * @param animation true为打开动画
     */
    public void setAnimation(boolean animation) {
        //isAnim = animation;
        //   mHandler.sendEmptyMessage(0);

    }


}

