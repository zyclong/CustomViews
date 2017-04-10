package com.zyclong.customviewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zyclong-pc on 2017/2/27.
 */

public class MutilCircleProgressBar extends View {
    private  int mCircleLineStrokeWidth = 4; //圆圈的粗细
    private  int mTxtStrokeWidth = 4;  //字体的粗细
    private String mText;
    private String mNoteAbove;
    private String mNoteBelow;
    private int mProgressColor;
    private float mShadowRadius=15.0f;
    private float mShadowDx=20f;
    private float mShadowDy=20f;
    private  RectF mRectF;
    private int mFirstColor=Color.BLUE;
    private int mSecondColor=Color.BLUE;
    private int mThirdColor=Color.RED;
    private int mInterval=30;

    public MutilCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRectF = new RectF();
        init(attrs);
    }

    public MutilCircleProgressBar(Context context) {
        super(context);
        mRectF = new RectF();
        init(null);
    }

    public MutilCircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRectF = new RectF();
        init(attrs);
    }

    /**
     * 初始化属性
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        if(null==attrs){
            mText="-mText-";
            mNoteAbove="-mNoteAbove-";
            mNoteBelow="-mNoteBelow-";
            mProgressColor= Color.RED;
        }
        TypedArray typeArray=getContext().obtainStyledAttributes(attrs, R.styleable.MutilCircle);
      ///  TypedArray typedArray= getContext().obtainStyledAttributes(attrs, R.styleable.MutilCircle);
        int counts=typeArray.getIndexCount();
        for(int i=0;i<counts;i++){
            int id=typeArray.getIndex(i);
            if(id==R.styleable.MutilCircle_android_text){
                mText=typeArray.getString(i);
                if(null==mText){
                    mText="-mText-";
                }
            }else if(id==R.styleable.MutilCircle_note_above){
                mNoteAbove=typeArray.getString(i);
                if(null==mNoteAbove){
                    mNoteAbove="-mNoteAbove-";
                }

            }else if(id==R.styleable.MutilCircle_note_below){
                mNoteBelow=typeArray.getString(i);
                if(null==mNoteBelow){
                    mNoteBelow="-mNoteBelow-";
                }

            }else if(id==R.styleable.MutilCircle_progress_color){
                mProgressColor=typeArray.getColor(i, Color.RED);

            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = Math.min(this.getWidth(),this.getHeight());
        width=width <= 100 ? 100 : width;
        int height = width;

        // 保存图层并全体偏移，让paddingTop和paddingLeft生效
      //  canvas.save();
       // canvas.translate(buttonLeft+OFFSETX+offsetx, buttonTop+offsety);

//        画第一个圆
        Paint firstPaint=new Paint();
        firstPaint.setColor(mFirstColor);
        firstPaint.setStyle(Paint.Style.FILL);
        firstPaint.setShadowLayer(mShadowRadius,mShadowDx,mShadowDy,mFirstColor);
        firstPaint.setStrokeWidth(mCircleLineStrokeWidth);
      //  mRectF.left = mCircleLineStrokeWidth / 2; // 左上角x
      //  mRectF.top = mCircleLineStrokeWidth / 2; // 左上角y
      //  mRectF.right = width- mCircleLineStrokeWidth / 2; // 左下角x
      //  mRectF.bottom = height - mCircleLineStrokeWidth / 2; // 右下角y
        //canvas.drawArc(mRectF, 0,  360, false, mPaint);
        canvas.drawCircle(width/2,height/2,width/2-2*mInterval,firstPaint);

        //画第二个圆
        Paint secondPaint=new Paint();
        secondPaint.setColor(mSecondColor);
        secondPaint.setStyle(Paint.Style.STROKE);
        PathEffect effects = new DashPathEffect(new float[] { 4, 8, 4, 8}, 1);
        secondPaint.setPathEffect(effects);
        canvas.drawCircle(width/2,height/2,width/2-mInterval,secondPaint );

    }
}
