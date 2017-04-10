package com.abellstarlite.wedgit.zyclong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.abellstarlite.bean.Interface.IProbleEventBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zyclong-pc on 2017/3/10.
 */

public class LineChart extends View {

    private final static String TAG="LineChart";
    private Display mDisplay;
    private float mTextSize=11;/*dp*/
    private float paddingPrecent=0;
    private int originX;/*x轴原点*/
    private int originY;/*Y轴原点*/
    private int mCalibrationY=5;/*Y轴的刻度数*/
    private Paint mPaintAxisX,mPaintAxisY,mPaintKindP,mPaintKindC,mPaintKindP_P,mPaintKindC_P,mPaintAxisButtomText;
    private int mColorAxisX,mColorAxisY,mColorAxisButtomText;
    private int mCalibrationX=120;/*x轴最大刻度*/
    private int mSmallestUnitXValue=12;/*x轴最小刻度值*/
    private String mTitleX="Pee frequency and change Diapers";/*左侧标注*/
    private String[] mBarButtomText=new String[]{
            "Date","Pee frequency","Diapers"
    };/* 底部标注*/
    private String pattern;/*日期字符串的正则表达式  例如 2017-12-25*/
    private Map<WeekOfMonth,int[]> mDataSourceMap;/*数据源*/
    private String mColorAxisYStr="#FFFFFF";/* Y轴的颜色*/
    private String[] mColorKindPStrs=new String[]{
            "#ffFFFFFF",  "#77FFFFFF", "#55FFFFFF", "#44FFFFFF","#88FFFFFF"
//            "#ffFFFFFF", "#00FFFFFF","#ffFFFFFF"
    };/* P事件的渐变颜色*/

    private String mColorKindPStr="#FFFFFF";/* P事件的颜色*/

    private String[] mColorKindCStrs=new String[]{
//            "#ffA90FFD" ,"#00A90FFD","#ffFFFFFF"
            "#ffA90FFD","#77A90FFD","#55A90FFD","#44FFFFFF","#ffFFFFFF"
    };/* C事件的渐变颜色*/
    private String mColorKindCStr="#ffA90FFD";/* C事件的颜色*/
    private int mColorKindC=Color.parseColor(mColorKindCStr);;/* C事件的颜色*/
    private int mColorKindP=Color.parseColor(mColorKindPStr);/* P事件的颜色*/
    private float mWidthPrecent=1.5f;/*宽度与高度的比值*/
    DisplayMetrics dw;
    private float mHeightPrecent=0.4f;/*默认的控件高度与手机屏幕高度的占比*/
    private float mStokeEventPrecent=0.005f;/*线条粗细，相对于控件高度*/


    public LineChart(Context context) {
        this(context,null);
    }

    public LineChart(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mColorAxisX= Color.WHITE;
        mPaintAxisX.setColor(mColorAxisX);

        mPaintAxisY=new Paint();
        mColorAxisY=Color.parseColor(mColorAxisYStr);
        mPaintAxisY.setColor(mColorAxisY);
        mPaintKindC=new Paint();
        mPaintKindC.setColor(mColorKindC);
        mPaintKindP=new Paint();
        mPaintKindP.setColor(mColorKindP);


        mPaintKindC_P=new Paint();
        mPaintKindC_P.setAntiAlias(true);
        mPaintKindC_P.setColor(mColorKindC);


        mPaintKindP_P=new Paint();
        mPaintKindP_P.setAntiAlias(true);
        mPaintKindP_P.setColor(mColorKindP);



        mPaintAxisButtomText=new Paint();
        mColorAxisButtomText=Color.WHITE;
        mPaintAxisButtomText.setColor(mColorAxisButtomText);

        pattern = "^\\d{4}-[01]\\d-[0123]\\d";/*日期字符串的正则表达式  例如 2017-12-25*/
        paddingPrecent=0.05f;


        dw = new DisplayMetrics();
        mDisplay.getMetrics(dw);
        mTextSize=dip2px(mTextSize);

    }

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
                measureHeight =getPaddingTop() + getPaddingBottom();
                break;
            case MeasureSpec.EXACTLY://如果heightSize是当前视图可使用的绝对宽度
                measureHeight = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.UNSPECIFIED://如果heightSize对当前视图宽度的计算没有任何参考意义
                measureHeight =  getPaddingTop() + getPaddingBottom();
                break;
            default:
                measureHeight =   getPaddingTop() + getPaddingBottom();
                break;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (measureWidth < getMeasuredWidth()) {
            measureWidth = getMeasuredWidth();
        }

        if (measureHeight < getMeasuredHeight()) {
            measureHeight = getMeasuredHeight();
        }

        measureHeight= (int) Math.max(measureHeight,dw.heightPixels*mHeightPrecent);
        measureWidth= (int) Math.max(measureHeight*mWidthPrecent,measureWidth);
        measureWidth= (int) Math.max(dw.widthPixels,measureWidth);

        Log.i(TAG, "measureWidth: "+measureWidth+"  measureHeight="+measureHeight);
        setMeasuredDimension(measureWidth, measureHeight);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
            /*x轴刻度 左边的字体大小*/
            int width= getWidth();
            /*计算坐标原点，以及x轴y轴的长度*/

            /*x轴为屏幕高度（height）的方向*/
            /* (originX ,originY) 为坐标轴原点坐标*/
            originX = (int) (paddingPrecent * width + mTextSize * 2);
            originY = (int) ((1 - paddingPrecent) * getHeight() -dip2px(45));
            int axisY = (int) (width - originX - paddingPrecent * width);
            int axisX = (int) (originY - paddingPrecent * getHeight());
            float smallestUnitY = axisY / mCalibrationY;
            float smallestUnitX = axisX / mCalibrationX;

             /*画x轴*/
            mPaintAxisX.setTextSize(mTextSize);
            for (int i = mCalibrationX; i > 0; i = i - mSmallestUnitXValue) {
                canvas.drawLine(originX, originY - smallestUnitX * i, originX + axisY, originY - smallestUnitX * i, mPaintAxisX);
                canvas.drawText(i + " ", originX - mPaintAxisX.measureText(i + " "), originY - smallestUnitX * i, mPaintAxisX);

            }
            canvas.drawLine(originX, originY, originX, originY - mCalibrationX*smallestUnitX, mPaintAxisX);

             /*画Y轴*/
            mPaintAxisY.setStyle(Paint.Style.FILL);
            mPaintAxisX.setTextSize(mTextSize);

            String str="-0";
            Path EventP=null;/*事件p的路径*/
            Path EventP_P=null;/*事件p的渐变效果的路径*/
            Path EventC=null;/*事件C的路径*/
            Path EventC_P=null;/*事件C的渐变效果的路径*/
            Rect textB=new Rect();
            for (int i = 0; i < mCalibrationY; i++) {


                float x,y;
                x=(float) (originX + smallestUnitY * (i + 0.5));
                y=(float) (originY + mTextSize+(getHeight()*0.01));
                str= WeekOfMonth.getValue(i);

                  /*画Y轴刻度数*/

                mPaintAxisX.getTextBounds(str,0,str.length(),textB);
                canvas.drawText(str, x-textB.width()/2, y, mPaintAxisX);

                 /*画Y轴刻度点*/
                canvas.drawCircle(x, originY, dip2px(3), mPaintAxisY);

                 /*计算两条折线的路径*/
                mPaintKindP_P.setStyle(Paint.Style.STROKE);
                mPaintKindC_P.setStyle(Paint.Style.STROKE);
                float CricleRudio= (float) (getHeight()*0.01);
                if(null!=mDataSourceMap) {
                    /*有数据时填充数据，也就是要记录折线路径*/
                    int[] ii = null;
                    ii = mDataSourceMap.get(WeekOfMonth.getInstance(i));
                    ii[0] = Math.min(ii[0], mCalibrationX);
                    ii[1] = Math.min(ii[1], mCalibrationX);
                    if (null == EventP) {
                        EventP = new Path();
                        EventP.moveTo(originX, originY-smallestUnitX);
                        EventP.lineTo(originX, originY - smallestUnitX * ii[0]);

                        EventC = new Path();
                        EventC.moveTo(originX, originY-smallestUnitX);
                        EventC.lineTo(originX, originY - smallestUnitX * ii[1]);

                        /*折线要延伸到控件左边缘*/
                        EventP_P = new Path();
                        EventP_P.moveTo(originX, originY - smallestUnitX * ii[0]);

                        EventC_P = new Path();
                        EventC_P.moveTo(originX, originY - smallestUnitX * ii[1]);

                    }
                    EventP.lineTo(x, originY - smallestUnitX * ii[0]);
                    EventP_P.lineTo(x, originY - smallestUnitX * ii[0]);
                    EventC.lineTo(x, originY - smallestUnitX * ii[1]);
                    EventC_P.lineTo(x, originY - smallestUnitX * ii[1]);

                    EventP_P.addCircle(x+CricleRudio, originY - smallestUnitX * ii[0],CricleRudio, Path.Direction.CCW);
                    EventC_P.addCircle(x+CricleRudio, originY - smallestUnitX * ii[1],CricleRudio, Path.Direction.CCW);
                    EventP.lineTo(x+CricleRudio*2, originY - smallestUnitX * ii[0]);
                    EventC.lineTo(x+CricleRudio*2, originY - smallestUnitX * ii[1]);

                    if(i==mCalibrationY-1){
                        /*折线要延伸到控件右边缘*/
                        EventP.lineTo(originX+axisY, originY - smallestUnitX * ii[0]);
                        EventP.lineTo(originX+axisY, originY+smallestUnitX);

                        EventC.lineTo(originX+axisY, originY - smallestUnitX * ii[1]);
                        EventC.lineTo(originX+axisY, originY+smallestUnitX);

                        EventP_P.lineTo(originX+axisY, originY - smallestUnitX * ii[0]);
                        EventC_P.lineTo(originX+axisY, originY - smallestUnitX * ii[1]);

                    }
                }

            }

            if(null!=EventC){
               /*画折线*/
                mPaintKindP_P.setStrokeWidth (getHeight()*mStokeEventPrecent);
                mPaintKindC_P.setStrokeWidth (getHeight()*mStokeEventPrecent);
                mPaintKindC_P.setStyle(Paint.Style.STROKE);
                mPaintKindP_P.setStyle(Paint.Style.STROKE);
                mPaintKindP_P.setAntiAlias(true);
                mPaintKindC_P.setAntiAlias(true);
                canvas.drawPath(EventP_P, mPaintKindP_P);
                canvas.drawPath(EventC_P, mPaintKindC_P);

                /*画渐变色*/
                LinearGradient lP=new LinearGradient(0,0,0,getHeight(),getColors(0),null, Shader.TileMode.CLAMP);
                LinearGradient lC=new LinearGradient(0,0,0,getHeight(),getColors(1),null, Shader.TileMode.CLAMP);
                mPaintKindP.setStyle(Paint.Style.FILL);
                mPaintKindC.setStyle(Paint.Style.FILL);
                mPaintKindP.setShader(lP);
                mPaintKindC.setShader(lC);
                canvas.drawPath(EventP, mPaintKindP);
                canvas.drawPath(EventC, mPaintKindC);



                canvas.drawLine(originX, originY, originX + axisY, originY, mPaintAxisY);
            }
            canvas.drawLine(originX, originY, originX + axisY, originY, mPaintAxisY);


            /*改变字体的方向*/
            float textSize=dip2px(11);
            float x, y;
            x=originX - mPaintAxisX.measureText(mCalibrationX + "1");
            mPaintAxisX.setTextSize(textSize);
            y=originY - axisX / 2 + mPaintAxisX.measureText(mTitleX) / 2;
            canvas.rotate(-90, x, y);
            canvas.drawText(mTitleX, x, y, mPaintAxisX);
            canvas.rotate(90, x,y);
            mPaintAxisX.setTextSize( mTextSize);


           /*画底部的标注文本*/
            mPaintKindC_P.setStyle(Paint.Style.FILL);
            mPaintKindP_P.setStyle(Paint.Style.FILL);
            textSize=Math.min(textSize,mTextSize);
            mPaintAxisButtomText.setTextSize(textSize);

            int baseDp=150;
            /*文本 Date*/
            canvas.drawText(mBarButtomText[0], dip2px(baseDp), originY + mTextSize*3, mPaintAxisButtomText);
            /*p事件矩形 */
            canvas.drawRect(dip2px(baseDp+40), originY + mTextSize*2,
                    dip2px(baseDp+40)+dip2px(15),originY + mTextSize*2+textSize, mPaintKindP_P);
            /*文本 Peefrequency*/
            canvas.drawText(mBarButtomText[1], dip2px(baseDp+59), originY + mTextSize*3, mPaintAxisButtomText);
             /*C事件矩形 */
            canvas.drawRect (dip2px(baseDp+138), originY + mTextSize *2,
                    dip2px(baseDp+138)+dip2px(15),  originY + mTextSize *2+textSize, mPaintKindC_P);

            canvas.drawText(mBarButtomText[2],dip2px(baseDp+157), originY + mTextSize*3, mPaintAxisButtomText);



        }catch (Exception e){
            Log.e(TAG, "onDraw: ",e );
        }

    }

    public  int dip2px(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;// 设备密度
        int px = (int) (dip * density + 0.5f);// 3.1->3, 3.9+0.5->4.4->4
        return px;
    }

    /**
     * 返回渐变色的颜色数组
     * */
    private int[] getColors(int index) {
        int [] colors=null;
       switch (index) {
           case 0:/*P事件渐变数组*/
               colors=getColors(mColorKindPStrs);
               break;
           case 1:/*C事件渐变数组*/
               colors=getColors(mColorKindCStrs);
               break;
           default:
               throw new IllegalArgumentException("not this index ："+index);
       }
        return colors;
    }

    private int[] getColors(String[] ColorStrs) {
        int[] colors= new int[ColorStrs.length];
        for(int i=0;i<ColorStrs.length;i++){
            colors[i]=Color.parseColor(ColorStrs[i]);
        }
        return colors;
    }




    /**
     * 设置View的数据
     * @param dataDayList 连续一个月的数据量
     * @param startTime 起始日期 格式为yyyy-MM-dd，例如 2017-01-12
     *
     */
    public void setDataSource(final ArrayList<IProbleEventBean> dataDayList, final String startTime ){
        try{
            checkParamas(dataDayList,startTime);
             final LineChartHolder linecharHolder=new LineChartHolder();
            /*Map<String ,int[]> int[]保存某天的P C事件总数*/

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mDataSourceMap=linecharHolder.format(dataDayList,startTime);
                    postInvalidate();
                }
            }).start();

        }catch (Exception e){
            Log.e(TAG, "setDataSource: ", e);
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

    static class LineChartHolder{

        private static final String EVENT_P="P";
        private static final String EVENT_C="C";
        private Map<String, int[]> mDataSourceMap;

        public HashMap<WeekOfMonth,int[]> format(ArrayList<IProbleEventBean> dataDayList, String startTime) {
             mDataSourceMap = formatDate(dataDayList);
            /*初始化map*/
           HashMap<WeekOfMonth,int[]> map=new HashMap<>();
            map.put(WeekOfMonth.FIFTH_WEEK,new int[]{0,0});
            map.put(WeekOfMonth.FIRST_WEEK,new int[]{0,0});
            map.put(WeekOfMonth.SECOND_WEEK,new int[]{0,0});
            map.put(WeekOfMonth.THIRD_WEEK,new int[]{0,0});
            map.put(WeekOfMonth.FORTH_WEEK,new int[]{0,0});
            String str=null,month=null;
            int dateInt=-1;
            month=startTime.substring(0,startTime.lastIndexOf("-"));
            for(String s: mDataSourceMap.keySet()){
                if(!s.startsWith(month)){
                    /*忽略年月不匹配的数据*/
                    Log.i(TAG, "format: 无效数据，年月（"+month+"）不匹配："+s);
                    continue;
                }
                str=s.substring(s.lastIndexOf("-")+1);
                dateInt=Integer.parseInt(str);
                switch(dateInt){
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        map.get(WeekOfMonth.FIRST_WEEK)[0]+=mDataSourceMap.get(s)[0];
                        map.get(WeekOfMonth.FIRST_WEEK)[1]+=mDataSourceMap.get(s)[1];
                        break;
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                        map.get(WeekOfMonth.SECOND_WEEK)[0]+=mDataSourceMap.get(s)[0];
                        map.get(WeekOfMonth.SECOND_WEEK)[1]+=mDataSourceMap.get(s)[1];
                        break;
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                        map.get(WeekOfMonth.THIRD_WEEK)[0]+=mDataSourceMap.get(s)[0];
                        map.get(WeekOfMonth.THIRD_WEEK)[1]+=mDataSourceMap.get(s)[1];
                        break;
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                        map.get(WeekOfMonth.FORTH_WEEK)[0]+=mDataSourceMap.get(s)[0];
                        map.get(WeekOfMonth.FORTH_WEEK)[1]+=mDataSourceMap.get(s)[1];
                        break;
                    case 29:
                    case 30:
                    case 31:
                        map.get(WeekOfMonth.FIFTH_WEEK)[0]+=mDataSourceMap.get(s)[0];
                        map.get(WeekOfMonth.FIFTH_WEEK)[1]+=mDataSourceMap.get(s)[1];
                        break;
                    default:
                }
            }
            return map;


        }

        public Map<String,int[]> formatDate(ArrayList<IProbleEventBean> dataDayList) {
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

    /**
     * 表示一个月包含的星期的枚举
     * 一共五周，第五周不满七天
     */
    public enum  WeekOfMonth{
        FIRST_WEEK("01-07"),
        SECOND_WEEK("08-14"),
        THIRD_WEEK("15-21"),
        FORTH_WEEK("22-28"),
        FIFTH_WEEK("29-31");

        private String value;
        private WeekOfMonth(String str){
            value=str;
        }

        public static String getValue(int index){
            switch (index){
                case 0:
                    return FIRST_WEEK.value;
                case 1:
                    return SECOND_WEEK.value;
                case 2:
                    return THIRD_WEEK.value;
                case 3:
                    return FORTH_WEEK.value;
                case 4:
                    return FIFTH_WEEK.value;
                default:
                    throw new IllegalArgumentException("OutOfBounder,WeekOfMonth don't have this index,index="+index) ;

            }
        }
        public static WeekOfMonth getInstance(int index){
            switch (index){
                case 0:
                    return FIRST_WEEK;
                case 1:
                    return SECOND_WEEK;
                case 2:
                    return THIRD_WEEK;
                case 3:
                    return FORTH_WEEK;
                case 4:
                    return FIFTH_WEEK;
                default:
                    throw new IllegalArgumentException("OutOfBounder,WeekOfMonth don't have this index,index="+index) ;

            }
        }
        public String getValue(){
            return this.value;
        }
    }

}

