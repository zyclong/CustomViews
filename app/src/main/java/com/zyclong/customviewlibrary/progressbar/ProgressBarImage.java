package com.zyclong.customviewlibrary.progressbar;

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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.zyclong.customview.R;


/**
 * Created by zyclong-pc on 2017/3/18.
 * 手机横竖屏问题未解决？？？
 *背景图片被改用在旋转图片
 * 参数的适配采用参照720x1080
 *
 * 注释了虚线圆部分的代码
 * 注释了自定义属性相关代码
 */


public class ProgressBarImage extends LinearLayout {


    private static final String TAG="ProgerssBarARC";
    private  float mPaddingPrecent=0;
    private Display mDisplay;

    /**
     * mPaintCircleBG 背景圆的画笔
     * mPaintCircleBordar 背景圆描边的画笔
     * mPaintCircleProgress 进度条画笔
     * mPaintCircleDotted 虚线圆的画笔
     * mPaintText 虚线圆的画笔
     *
     * */
    private Paint mPaintCircleBG,mPaintCircleBordar,mPaintCircleProgress,mPaintCircleDotted,mPaintText;
    private int mRaidus;/*背景圆半径，其他圆的半径取与它的相对值*/
    private int mColorCircleBG= Color.parseColor("#51CBFE");/*背景圆的颜色*/
    private int mStrokeCircleBG=25;/*背景圆的宽度*/
    private float mStartPosition=135f;/*进度条开始的角度*/
    private float mRadian=270;/*进度条的弧度*/

    private String mTextColor="#2FBCFC";
    private int mColorCircleBGBordar= Color.WHITE;/*背景圆的颜色*/
    private int mStrokeCircleBGBordar= (int) (mStrokeCircleBG*0.15);/*背景圆的宽度*/
    private int mProgress=88;/* 0 ~ 100,表示进度*/
    private float mDottedLength=8;/*虚线圆中点的长度*/
    private float mDottedInterval=15;/*虚线圆中点与点的间隔*/
    private float mTextSize=1f ;/*进度文本的TextSize，默认是控件宽度*mTextSize_mRaidus */
    private float mTextSize_mRaidus=0.6f;/*调整进度文本的TextSize的比例*/
    private int mPadding=0;/*四边的padding最小值*/
    private Matrix mMatrix=new Matrix();/*设置渐变属性*/
    /*进度条渐变色*/
    private int[] mColorsShader= new int[]{
            Color.parseColor("#0EB7DE"),
            Color.YELLOW,
            Color.YELLOW,
            Color.YELLOW
    };
    // private float mIntervalImageDotted;
    private float mIntervalImageDottedPrecent=-0.05f;
    private Drawable mBackgroundDrawable;

    private Handler mHandler;
    private long mDelayMillis=1/360*100;/*重绘的时间*/
    private float mDegrees=0;

    private boolean isAnim=true;


    public ProgressBarImage(Context context) {
        this(context,null);
    }

    public ProgressBarImage(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressBarImage(Context context, AttributeSet attrs, int defStyleAttr) {
//        attrs
        super(context,attrs,defStyleAttr);
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

            mHandler =new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    //   Log.i(TAG, "handleMessage: invalidate(); ");
                    mDegrees=rotation(mDegrees);
                    invalidate();
                }
            };
            AdaptationParams();

        }catch (Exception e){
            Log.e(TAG, "ProgerssBarARC: ",e );
        }
    }

    /*重绘时，旋转的角度*/
    private float rotation(float Degrees) {
        Degrees+=1;
        return Degrees%360;
    }

    /**
     * 按屏幕分辨率改变参数
     */
    private void AdaptationParams() {
        int width=0,baseWidth=720;
        int height=0,baseHeight=1080;

       // l.set

        try {
            mDisplay = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics d=new DisplayMetrics();
            //display.getOrientation();
            mDisplay.getMetrics(d);
            width=d.widthPixels;
            height=d.heightPixels;
            mStrokeCircleBG= (int) AdaptationParam(mStrokeCircleBG,width/baseWidth);/*背景圆的宽度*/
            mStartPosition= (float) AdaptationParam(mStartPosition,width/baseWidth);
            mStrokeCircleBGBordar= (int) (mStrokeCircleBG*0.15);/*背景圆的宽度*/
            mDottedLength= (float) AdaptationParam(mDottedLength,width/baseWidth);
            mDottedInterval= (float) AdaptationParam(mDottedInterval,width/baseWidth);

            mPadding= (int) (mPaddingPrecent*Math.min(width,height));
            mTextSize=mTextSize*height;
            mProgress=Math.min(mProgress,100);


        }catch (Exception e){
            Log.e(TAG, "AdaptationParams: ",e );
        }


    }
    private double AdaptationParam(double t,double proportion ){
        return t*proportion;
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

     //   LinearLayout .LayoutParams l= (LayoutParams) getLayoutParams();
//        l.setMargins((int)(getWidth()/2-mRaidus*mPaddingPrecent),
//               (int)(getHeight()/2-mRaidus*mPaddingPrecent),
//               (int)(getWidth()/2-mRaidus*mPaddingPrecent),
//               (int)(getHeight()/2-mRaidus*mPaddingPrecent));
    }




    @Override
    protected void onDraw(Canvas canvas) {

        try {
            int width = getWidth();
            int height = getHeight();
            width  = width < height  ? width : height;



            //画背景圆
          //  mRaidus=width/2-mPadding-mStrokeCircleBG;

            mTextSize= mTextSize>mRaidus*mTextSize_mRaidus? (float) (mRaidus * mTextSize_mRaidus) :mTextSize;
            mPaintCircleBG=new Paint();
            mPaintCircleBG.setColor(mColorCircleBG);
            mPaintCircleBG.setStrokeWidth(mStrokeCircleBG);
            RectF oval = new RectF();/*确定绘图区域*/
            oval.left = getWidth()/2-mRaidus;
            oval.top = getHeight()/2-mRaidus;
            oval.right =getWidth()/2+mRaidus;
            oval.bottom =getHeight()/2+mRaidus;
            // Log.i(TAG, "onDraw: "+(oval.right-oval.left)+"   .bottom-oval.top"+(oval.bottom-oval.top)+" mRaidus"+mRaidus);
            mPaintCircleBG.setStrokeCap(Paint.Cap.ROUND);
            mPaintCircleBG.setStyle(Paint.Style.STROKE);
            canvas.drawArc(oval, mStartPosition, mRadian, false, mPaintCircleBG);


            //画背景进度条圆
            mPaintCircleProgress=new Paint();
            mPaintCircleProgress.setStrokeWidth(mStrokeCircleBG);
            mPaintCircleProgress.setStrokeCap(Paint.Cap.ROUND);
            mPaintCircleProgress.setStyle(Paint.Style.STROKE);
    /*渐变颜色旋转mStartPosition-10，包裹圆角*/
            mMatrix.setRotate(mStartPosition-5,oval.centerX(),oval.centerY());

            SweepGradient shader = new SweepGradient(oval.centerX(), oval.centerY(),
                    mColorsShader,null
            );
            shader.setLocalMatrix(mMatrix);
            mPaintCircleProgress.setShader(shader);
            canvas.drawArc(oval, mStartPosition,mProgress/100f*mRadian, false, mPaintCircleProgress);


            //画背景圆的bordar(一个封闭的环形，两个弧，两个半圆)
            mPaintCircleBordar=new Paint();
            mPaintCircleBordar.setStrokeWidth((float) (mStrokeCircleBGBordar));
            mPaintCircleBordar.setColor(mColorCircleBGBordar);
            mPaintCircleBordar.setStrokeCap(Paint.Cap.ROUND);
            mPaintCircleBordar.setStyle(Paint.Style.STROKE);

            RectF ovalBordar = new RectF();

            ovalBordar.left = oval.left-mStrokeCircleBG / 2;
            ovalBordar.top = oval.top-mStrokeCircleBG / 2;
            ovalBordar.right =oval.right+mStrokeCircleBG / 2;
            ovalBordar.bottom = oval.bottom+mStrokeCircleBG / 2;
            canvas.drawArc(ovalBordar, mStartPosition, mRadian, false, mPaintCircleBordar);

            ovalBordar.left = oval.left+mStrokeCircleBG/2 ;
            ovalBordar.top = oval.top+mStrokeCircleBG/2 ;
            ovalBordar.right =oval.right-mStrokeCircleBG / 2;
            ovalBordar.bottom = oval.bottom-mStrokeCircleBG / 2;
            canvas.drawArc(ovalBordar, mStartPosition, mRadian, false, mPaintCircleBordar);

            float x,y;/*半圆的圆心坐标*/
            x=(float) (oval.centerX()+mRaidus*Math.cos( Math.toRadians(mStartPosition+mRadian)));
            y=(float) (oval.centerY()+mRaidus*Math.sin( Math.toRadians(mStartPosition+mRadian)));
            ovalBordar.left = x-mStrokeCircleBG/2 ;
            ovalBordar.top =y-mStrokeCircleBG/2 ;
            ovalBordar.right =x+mStrokeCircleBG / 2;
            ovalBordar.bottom = y+mStrokeCircleBG / 2;
            canvas.drawArc(ovalBordar, mStartPosition+mRadian, 180, false, mPaintCircleBordar);

            x=(float) (oval.centerX()+mRaidus*Math.cos( Math.toRadians(mStartPosition)));
            y=(float) (oval.centerY()+mRaidus*Math.sin( Math.toRadians(mStartPosition)));
            ovalBordar.left = x-mStrokeCircleBG/2 ;
            ovalBordar.top =y-mStrokeCircleBG/2 ;
            ovalBordar.right =x+mStrokeCircleBG / 2;
            ovalBordar.bottom = y+mStrokeCircleBG / 2;
            canvas.drawArc(ovalBordar, 180+mStartPosition, 180, false, mPaintCircleBordar);

//    //画背景Dotted圆
//    mPaintCircleDotted=new Paint();
            RectF ovalDotted=new RectF();
            ovalDotted.left=oval.left+mStrokeCircleBG+mStrokeCircleBGBordar;
            ovalDotted.right=oval.right-mStrokeCircleBG-mStrokeCircleBGBordar;
            ovalDotted.top=oval.top+mStrokeCircleBG+mStrokeCircleBGBordar;
            ovalDotted.bottom=oval.bottom-mStrokeCircleBG-mStrokeCircleBGBordar;
//    mPaintCircleDotted.setColor(mColorCircleBGBordar);
            // mPaintCircleDotted.setStrokeCap(Paint.Cap.ROUND);
            //  mPaintCircleDotted.setStrokeWidth(mStrokeCircleBGBordar);
            //  mPaintCircleDotted.setStyle(Paint.Style.STROKE);
            //  Path p=new Path();
            //  p.addArc(ovalDotted,0,360);
            //  DashPathEffect pdpe=new DashPathEffect(new float[]{mDottedLength,mDottedInterval},0);
            //   mPaintCircleDotted.setPathEffect(pdpe);
//    canvas.drawPath(p,mPaintCircleDotted);

    /*画图，旋转*/
            RectF ovalImage=new RectF();
            ovalImage.left=  ovalDotted.left +  mIntervalImageDottedPrecent*mRaidus;
            ovalImage.right= ovalDotted.right- mIntervalImageDottedPrecent*mRaidus;
            ovalImage.top=   ovalDotted.top  + mIntervalImageDottedPrecent*mRaidus;
            ovalImage.bottom=ovalDotted.bottom-mIntervalImageDottedPrecent*mRaidus;
            Rect rect=new Rect();
            rect.left= (int) ovalImage.left;
            rect.right= (int) ovalImage.right;
            rect.top= (int) ovalImage.top;
            rect.bottom= (int) ovalImage.bottom;
            if(mBackgroundDrawable==null){
                mBackgroundDrawable=getResources().getDrawable(R.mipmap.diaper_icon_percent_of_usage_pion);
                return;
            }
            mBackgroundDrawable.setBounds(rect);
            canvas.rotate(mDegrees, rect.centerX(), rect.centerY());
//
          //  mBackgroundDrawable.draw(canvas);
            canvas.rotate(-mDegrees, rect.centerX(), rect.centerY());
            if(isAnim){
               // mHandler.sendEmptyMessageDelayed(0,mDelayMillis);
            }

     /*画进度值文本*/
            mPaintText=new Paint();
            mPaintText.setColor(Color.parseColor(mTextColor));
            mPaintText.setTextSize(mTextSize);
            float f=mPaintText.measureText(mProgress+"%");

            canvas.drawText(mProgress+"%",oval.centerX()-f/2,oval.centerY()+ mPaintText.descent(),mPaintText);


            super.onDraw(canvas);



        }catch (Exception e){
            Log.e(TAG, "onDraw: ",e );
        }


    }


    /**
     * 劫持背景图片，把背景图作为园内动画的图片
     * @param background
     */
    @Override
    public void setBackground(Drawable background) {
        mBackgroundDrawable =background;
        //super.setBackground(background);
    }

    /**
     * 保证在主线程调用
     * @param mProgress
     */
    public void setmProgress(int mProgress) {
        if(mProgress>100){
            mProgress=100;
        }
        if(mProgress<0){
            mProgress=0;
        }
        this.mProgress = mProgress;
        invalidate();
    }
    /**
     * 保证在非主线程调用
     * @param mProgress
     */
    public void setmProgressNotUI(int mProgress) {
        if(mProgress>100){
            mProgress=100;
        }
        if(mProgress<0){
            mProgress=0;
        }
        this.mProgress = mProgress;
        postInvalidate();
    }

    /**
     * 旋转动画的开关
     * @param animation true为打开动画
     */
    public void setAnimation(boolean animation) {
        isAnim = animation;
        invalidate();
    }

  //  @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//       // View v=getChildAt(0);
//      // int  padding= 0;
//
//       // v.layout(l+padding,t+padding,r-padding,b-padding);
//    }

//    private Bitmap getBitmapFromDrawable(Drawable drawable){
//        if(drawable == null){
//            return null;
//        }
//
//        if(drawable instanceof DrawableContainer) {
//            return getBitmapFromDrawable(drawable.getCurrent());
//        }else if(drawable instanceof BitmapDrawable) {
//            return ((BitmapDrawable) drawable).getBitmap();
//        }else{
//            return null;
//        }
//    }
//
//    private static Bitmap ReSize(Bitmap bitmap, float precent) {
//        Matrix matrix = new Matrix();
//        matrix.postScale(precent,precent); //长和宽放大缩小的比例
//        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
//        return resizeBmp;
//    }


}

