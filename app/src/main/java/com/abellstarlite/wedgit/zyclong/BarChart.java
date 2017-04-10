package com.abellstarlite.wedgit.zyclong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.abellstarlite.bean.Interface.IProbleEventBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by zyclong-pc on 2017/3/9.
 *
 * 尺寸参数采用比例，基数为屏幕的分辨率
 */

public class BarChart extends View{

    private final static String TAG="BarChart";
    private Display mDisplay;
    private float mTextSize;
    private float paddingPrecent;
    private int originX;
    private int originY;
    private int mCalibrationY=7;
    private Paint mPaintAxisX,mPaintAxisY,mPaintKindP,mPaintKindC,mPaintAxisButtomText;
    private Paint mPaintKindP_Cir,mPaintKindC_Cir,mPaintKindP_shader,mPaintKindC_shader;
    private int mColorAxisX,mColorAxisY,mColorAxisButtomText;
    private int mCalibrationX=20;
    private int mSmallestUnitXValue=2;
    private String mTitleX="Pee frequency and change Diapers";
    private String[] mBarButtomText=new String[]{
            "Date","Pee frequency","Diapers"
    };
    private String pattern;
    Map<String ,int[]> mDataSourceMap;
    private String mStarTime;
    private String mColorAxisXStr="#FFFEFE";/* y轴的颜色*/
    private String mColorAxisYStr="#FFFEFE";/* y轴的颜色*/

    private String[] mColorKindCStrs=new String[]{
            "#7AFED1","#CcFFEe"
    };/* C事件的颜色渐变数组*/
    private String mColorKindCStr=mColorKindCStrs[0];/* C事件的颜色*/

    private String[] mColorKindPStrs=new String[]{
            "#E376E2", "#FfCfFf"
    };/* P事件的颜色渐变数组*/
    private String mColorKindPStr=mColorKindPStrs[0];/* P事件的颜色*/

    private Shader mShaderC;
    private Shader mShaderP;

    private String mDatePattern="yyyy-MM-dd" ;/*日期格式*/
   // private String mPattern="yyyy-MM-dd HH:mm:ss" ;/*日期格式*/
    private SimpleDateFormat sdfOneDay;
    /*柱状图与间隔的比例：P事件柱状图Pp : P事件柱状图Pc : 柱状图间隔Pi=mBarProportion : mBarProportion : (1-mBarProportion*2)*/
    private float mBarProportion=0.27f;
    /*柱状图箭头的高度比例，mRadiusArcProportion=1时，柱状图箭头的高度为mBarProportion*Y轴的最小刻度（相邻两个点之间的距离）*/
    private float mRadiusArcProportion=0.4f;


    public BarChart(Context context) {
        this(context, null);
    }

    public BarChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(null!=attrs){
            init(attrs);
            
        }
    }

    private void init(AttributeSet attrs) {
        mDisplay = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics d=new DisplayMetrics();
        mDisplay.getMetrics(d);
        mPaintAxisX=new Paint();
        mColorAxisX=Color.parseColor(mColorAxisXStr);
        mPaintAxisX.setColor(mColorAxisX);

        mPaintAxisY=new Paint();
        mColorAxisY=Color.parseColor(mColorAxisYStr);
        mPaintAxisY.setColor(mColorAxisY);
        mPaintKindC=new Paint();
        mPaintKindC.setColor(Color.parseColor(mColorKindCStr));
        mPaintKindC.setStyle(Paint.Style.FILL);
        mPaintKindC.setAntiAlias(true);

        mPaintKindC_Cir=new Paint();
        mPaintKindC_Cir.setColor(Color.parseColor(mColorKindCStrs[mColorKindCStrs.length-1]));
        mPaintKindC_Cir.setStyle(Paint.Style.FILL);

        mPaintKindP=new Paint();
        mPaintKindP.setColor(Color.parseColor(mColorKindPStr));
        mPaintKindP.setStyle(Paint.Style.FILL);
        mPaintKindP.setAntiAlias(true);

        mPaintKindP_Cir=new Paint();
        mPaintKindP_Cir.setColor(Color.parseColor(mColorKindPStrs[mColorKindPStrs.length-1]));
        mPaintKindP_Cir.setStyle(Paint.Style.FILL);


        mPaintAxisButtomText=new Paint();
        mColorAxisButtomText=Color.WHITE;
        mPaintAxisButtomText.setColor(mColorAxisButtomText);

        mShaderC=getShader(mColorKindCStrs);
        mPaintKindC_shader=new Paint();
        mPaintKindC_shader.setShader(mShaderC);
        mPaintKindC_shader.setStyle(Paint.Style.FILL);


        mShaderP=getShader(mColorKindPStrs);
        mPaintKindP_shader=new Paint();
        mPaintKindP_shader.setShader(mShaderP);
        mPaintKindP_shader.setStyle(Paint.Style.FILL);

        pattern = "^\\d{4}-[01]\\d-[0123]\\d";/*日期字符串的正则表达式  例如 2017-12-25*/
        paddingPrecent=0.05f;

    }

    private Shader getShader(String[] ColorStrs) {
        int[] colors= new int[ColorStrs.length];
        for(int i=0;i<ColorStrs.length;i++){
            colors[i]=Color.parseColor(ColorStrs[i]);
        }
        LinearGradient shader=new LinearGradient(
                mDisplay.getWidth(),mDisplay.getHeight(),0,0,colors,null,Shader.TileMode.CLAMP);
        return shader;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
            /*x轴刻度 左边的字体大小*/
            mTextSize =dip2px(11);
             canvas.save();
            /*计算坐标原点，以及x轴y轴的长度*/
            originX = (int) (paddingPrecent * getWidth() + mTextSize * 2);
            originY = (int) ((1 - paddingPrecent) * getHeight() - mTextSize * 3);
            int axisY = (int) (getWidth() - originX - paddingPrecent * getWidth());
            int axisX = (int) (originY - paddingPrecent * getHeight());
            int smallestUnitY = axisY / mCalibrationY;
            int smallestUnitX = axisX / mCalibrationX;

             /*画x轴*/
            mPaintAxisX.setTextSize(mTextSize);
            for (int i = mCalibrationX; i > 0; i = i - mSmallestUnitXValue) {
                canvas.drawLine(originX, originY - smallestUnitX * i, originX + axisY, originY - smallestUnitX * i, mPaintAxisX);
                canvas.drawText(i + " ", originX - mPaintAxisX.measureText(i + " "), originY - smallestUnitX * i, mPaintAxisX);

            }
            canvas.drawLine(originX, originY, originX, originY - axisX, mPaintAxisX);

             /*画Y轴*/
            mPaintAxisY.setStyle(Paint.Style.FILL);

            /*有数据时填充数据*/
            Iterator iterator=null;
            String str="-0";

            Calendar calendar=null;
            if(null!=mStarTime){
                calendar = Calendar.getInstance();
                calendar.setTime(getOneDayFromString(mStarTime));
            }

            float mRadiusArc=  (smallestUnitY*mBarProportion)*mRadiusArcProportion;
            for (int i = 0; i < mCalibrationY; i++) {
                    /*画Y轴刻度数*/
                if(null!=calendar){
                    str=getOneDayOfString(calendar.getTime());
                    calendar.add(Calendar.DATE,1);
                }

                float x,y;
                x=(float) (originX + smallestUnitY * (i + 0.5));
                y=(float) (originY + mTextSize+(getHeight()*0.01));
                canvas.drawText(str.substring(str.lastIndexOf("-")+1), x-mPaintAxisX.measureText(str.substring(str.lastIndexOf("-")+1))/2, y, mPaintAxisX);

                /*画柱状图*/
                mPaintKindP_shader.setStrokeWidth(smallestUnitY*mBarProportion);
                mPaintKindC_shader.setStrokeWidth(smallestUnitY*mBarProportion);
                mPaintKindP_shader.setAntiAlias(true);
                mPaintKindC_shader.setAntiAlias(true);
                mPaintKindP_Cir.setAntiAlias(true);
                mPaintKindC_Cir.setAntiAlias(true);
                RectF oval = new RectF();/*确定箭头的绘图区域*/
                if(null!=mDataSourceMap&&null!=mDataSourceMap.get(str)){
//                    Log.i(TAG, "onDraw: mDataSourceMap.get(str)[0]"+mDataSourceMap.get(str)[0]);
                    /*不能超过mCalibrationX*/
                    mDataSourceMap.get(str)[0]=Math.min(mDataSourceMap.get(str)[0],mCalibrationX);
                    if( mDataSourceMap.get(str)[0]!=0){
                        /*箭头高度不能大于柱状图的高度*/
                        mRadiusArc=Math.min(smallestUnitX*mDataSourceMap.get(str)[0],mRadiusArc);
                        mPaintKindP_shader.setShader(new LinearGradient(
                                (float)(x-smallestUnitY*mBarProportion/2),
                                (float)originY,
                                x-smallestUnitY*mBarProportion/2,
                                originY-smallestUnitX*mDataSourceMap.get(str)[0]+mRadiusArc,

                                getColors(mColorKindPStrs),
                                null,
                                Shader.TileMode.CLAMP
                        ));
                        canvas.drawLine( x-smallestUnitY*mBarProportion/2,originY-smallestUnitX*mDataSourceMap.get(str)[0]+mRadiusArc,(float)(x-smallestUnitY*mBarProportion/2),(float)originY,mPaintKindP_shader);

                    /*画柱状图顶部的箭头形状*/
                        oval.left = x-smallestUnitY*mBarProportion;
                        oval.top = originY-smallestUnitX*mDataSourceMap.get(str)[0];
                        oval.right =x;
                        oval.bottom =originY-smallestUnitX*mDataSourceMap.get(str)[0]+(float)(mRadiusArc*2.1);
                        canvas.drawArc(oval,-180,180,true,mPaintKindP_Cir);
                        /*还原箭头高度*/
                        mRadiusArc=(smallestUnitY*mBarProportion)*mRadiusArcProportion;
                    }
//                    Log.i(TAG, "onDraw: mDataSourceMap.get(str)[1]"+mDataSourceMap.get(str)[1]);

                    /*不能超过mCalibrationX*/
                    mDataSourceMap.get(str)[1]=Math.min(mDataSourceMap.get(str)[1],mCalibrationX);
                    if( mDataSourceMap.get(str)[1]!=0) {
                        /*箭头高度不能大于柱状图的高度*/
                        mRadiusArc=Math.min(smallestUnitX*mDataSourceMap.get(str)[1],mRadiusArc);
                        mPaintKindC_shader.setShader(new LinearGradient(
                                (float) (x + smallestUnitY * mBarProportion / 2),
                                originY,
                                x + smallestUnitY * mBarProportion / 2,
                                originY - smallestUnitX * mDataSourceMap.get(str)[1] + mRadiusArc,
                                getColors(mColorKindCStrs),
                                null,
                                Shader.TileMode.CLAMP
                        ));
                        canvas.drawLine(x + smallestUnitY * mBarProportion / 2, originY - smallestUnitX * mDataSourceMap.get(str)[1] + mRadiusArc, (float) (x + smallestUnitY * mBarProportion / 2), originY, mPaintKindC_shader);
                    /*画柱状图顶部的箭头形状*/
                        oval.left = x;
                        oval.top = originY - smallestUnitX * mDataSourceMap.get(str)[1];
                        oval.right = x + smallestUnitY * mBarProportion;
                        oval.bottom = originY - smallestUnitX * mDataSourceMap.get(str)[1] + (float)(mRadiusArc*2.1);
                        canvas.drawArc(oval, -180, 180, true, mPaintKindC_Cir);
                          /*还原箭头高度*/
                        mRadiusArc=(smallestUnitY*mBarProportion)*mRadiusArcProportion;
                    }
                }

                 /*画Y轴刻度点*/
                canvas.drawCircle(x, originY, (float) dip2px(3), mPaintAxisY);

            }
            canvas.drawLine(originX, originY, originX + axisY, originY, mPaintAxisY);


            /*改变字体的方向*/
            float textSize=dip2px(11);
            mPaintAxisButtomText.setTextSize(textSize);
            canvas.rotate(-90, originX - mPaintAxisX.measureText(mCalibrationX + "1"), originY - axisX / 2 + mPaintAxisButtomText.measureText(mTitleX) / 2);
            canvas.drawText(mTitleX, originX - mPaintAxisButtomText.measureText(mCalibrationX + "1"), originY - axisX / 2 + mPaintAxisButtomText.measureText(mTitleX) / 2, mPaintAxisButtomText);
            canvas.rotate(90, originX - mPaintAxisX.measureText(mCalibrationX + "1"), originY - axisX / 2 + mPaintAxisButtomText.measureText(mTitleX) / 2);

            /*画底部的标注文本*/

            mPaintAxisButtomText.setTextSize(textSize);

            int baseDp=150;
            /*文本 Date*/
            canvas.drawText(mBarButtomText[0], dip2px(baseDp), originY + mTextSize*3, mPaintAxisButtomText);
            /*p事件矩形 */
            canvas.drawRect(dip2px(baseDp+40), originY + mTextSize*2,
                    dip2px(baseDp+40)+dip2px(15),originY + mTextSize*2+textSize, mPaintKindP);
            /*文本 Peefrequency*/
            canvas.drawText(mBarButtomText[1], dip2px(baseDp+59), originY + mTextSize*3, mPaintAxisButtomText);
             /*C事件矩形 */
            canvas.drawRect (dip2px(baseDp+135), originY + mTextSize *2,
                    dip2px(baseDp+138)+dip2px(15),  originY + mTextSize *2+textSize , mPaintKindC);

            canvas.drawText(mBarButtomText[2],dip2px(baseDp+157), originY + mTextSize*3, mPaintAxisButtomText);




        }catch (Exception e){
            Log.e(TAG, "onDraw: ",e );
        }

    }

    public  int dip2px(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;// 设备密度
//        Log.i(TAG, "dip2px: "+density);
        int px = (int) (dip * density + 0.5f);// 3.1->3, 3.9+0.5->4.4->4
        return px;
    }

    private int[] getColors(String[] ColorStrs) {
        int[] colors= new int[ColorStrs.length];
        for(int i=0;i<ColorStrs.length;i++){
            colors[i]=Color.parseColor(ColorStrs[i]);
        }
        return colors;
    }

    /**
     * 获取日期，字符格式为"yyyy-MM-dd"
     * @param event_time
     * @return
     */
    private Date getOneDayFromString(String event_time) {
        Date date=null;
        if(sdfOneDay==null) {
            sdfOneDay = new SimpleDateFormat(mDatePattern);
        }
        try {
            date = sdfOneDay.parse(event_time);
        } catch (ParseException e) {
            Log.e(TAG, "getDateFromString: ",e );
        }
        return date;
    }

    /**
     *字符串格式的日期
     * @param event_time Date
     * @return 获取日期，字符格式为"yyyy-MM-dd"
     */
    private String getOneDayOfString(Date event_time) {
        if(sdfOneDay==null) {
            sdfOneDay = new SimpleDateFormat(mDatePattern);
        }
        return sdfOneDay.format(event_time);
    }

    /**
     * 设置View的数据
     * @param dataDayList 连续七天的数据量
     * @param startTime 起始日期 格式为yyyy-MM-dd，例如 2017-01-12
     *
     */
    public void setDataSource(final ArrayList<IProbleEventBean> dataDayList, String startTime ){
        try{
            checkParamas(dataDayList,startTime);

            final BarChartHolder barcharHolder=new BarChartHolder();
            /*Map<String ,int[]> int[]保存某天的P C事件总数*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mDataSourceMap = barcharHolder.format(dataDayList);
                    postInvalidate();
                }
            }).start();

            mStarTime=startTime;

        }catch (Exception e){
            Log.e(TAG, "setDataSource: ", e);
            mStarTime=startTime;
            if(e.getMessage().contains("startTime")){
                mStarTime=null;
            }
            mDataSourceMap=null;


        }finally{
            invalidate();
        }

    }


    /**
     * 检验参数是否合法
     * @param dataDayList 不为空
     * @param startTime 不为空，匹配模式"^\\d{4}-[01]\\d-[0123]\\d"
     */
    private void checkParamas(ArrayList<IProbleEventBean> dataDayList, String startTime) {
        if(dataDayList==null||dataDayList.size()==0){
            throw new IllegalArgumentException(this.getClass().getSimpleName()+" don't get any datas,dataDayList is empty or null ");
        }
        if(null==startTime){
            throw new IllegalArgumentException(this.getClass().getSimpleName()+" don't get the startTime, startTime is null");
        }
        //yyyy-MM-dd HH:mm:ss

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(startTime);
        if(!m.matches()){
            throw new IllegalArgumentException("startTime is not matche ,like yyyy-MM-dd ");
        }
    }

    static class BarChartHolder{

        private static final String EVENT_P="P";
        private static final String EVENT_C="C";

        public Map<String,int[]> format(ArrayList<IProbleEventBean> dataDayList) {
            Map<String,int[]> map=new TreeMap<>();
            String timeStr=null;
            for(IProbleEventBean ipeb: dataDayList){
                timeStr=ipeb.getEvent_time().substring(0,ipeb.getEvent_time().indexOf(" "));
                add(timeStr,map);
                switch (ipeb.getKind()){
                    case EVENT_P:
                        map.get(timeStr)[0]++;
                        break;
                    case EVENT_C:
                        map.get(timeStr)[1]++;
                        break;
                    default:
                        break;
                }
            }
            return map;
        }
        private void add(String startTime, Map<String, int[]> map) {
            if(!map.containsKey(startTime)){
                map.put(startTime,new int[]{0,0});
            }
        }

    }
}
