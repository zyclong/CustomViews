package com.abellstarlite.wedgit.zyclong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.abellstarlite.bean.Interface.IProbleEventBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by zyclong-pc on 2017/3/1.
 * 没有自定义属性
 * 提交：2017.3.8 11:04
 */

public class DataDayView extends View {

    private final static String TAG="DataDayView";
    private static final String EVENT_P = "P";
    private static final String EVENT_C = "C";
    private static final int EVENT_P_IC= com.zyclong.customviewlibrary.R.drawable.data_icon_pee_time;
    private static final int EVENT_C_IC= com.zyclong.customviewlibrary.R.drawable.data_icon_diaper_time;
    private int mLineLenngth;
    private int mBGColor;
    private Paint bg;
    private int mInterval;
    private int mIntervalFrame;
    private int mLineNumber;
    private int mLineColor;
    private Paint mPaintLine;

    private int mOffsetY;
    private int mOffsetRed;
    private float mRadiusFill;
    private Paint mTextPaint;
    private int mTextSize;
    private float mLineStrokeWidth;
    private RectF mTriangleRectF;
    private int mOffsetGreen;
    private int mBaseWidth;
    private String pattern ;
    private DataDayViewHelper mDataDayViewHelper;
    private ArrayList<IProbleEventBean> mDataDay;
    private double[] mOffsetDataDay;
    private String mStartTime;
    private static final long mDayMilliSecond=24*60*60*1000;
    private float mBaseDistance;
    private String mWarrningText;
    private DataDayTextView mDataDayTextView;
    private float mTextoffsetX;
    private Thread thread;


    public DataDayView(Context context) {
       this(context,null,0);
    }


    public DataDayView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public DataDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams();
        init(attrs);
    }

    private void init(AttributeSet attrs) {
    }

    private void initParams() {
        mBGColor= Color.WHITE; //背景色
        mLineColor=Color.parseColor("#52cbff");  //每条线颜色和空心圆的

        bg=new Paint();/*背景画笔*/
        mPaintLine=new Paint();/*线条画笔*/
        mTextPaint=new Paint();/*文字画笔*/
        mLineNumber=5;/*线条数目*/

        mTextoffsetX=8f;/*字体与线条的距离*/
        mLineStrokeWidth=1f;//线条的粗细 dp
        mLineLenngth=640; //每条线的长度,控件的默认高度,dp
        mTextSize =9;/*文本的粗细 dp*/
        mOffsetY=15;/*线条在Y方向的偏移量*/

        mInterval=15;/*(mLineNumber指定线条数)最左边线条到最右边线条的间隔占控件宽度的比重，计算mInterval/（mInterval+mIntervalFrame）*/
        mIntervalFrame=2;/*两边间隔占控件宽度的比重，计算mIntervalFrame/（mInterval+mIntervalFrame）*/

        mOffsetRed=24*60;/*每个圆的间隔*/
        mOffsetGreen=24*60;/*每个圆的间隔*/
      //  mTextOffset=20;
        mRadiusFill=6f; /*实心圆半径*/

        mTriangleRectF=new RectF();/*圆锥，就是那个三角形*/
        mBaseWidth=720;/*宽度基准*/
        pattern = "^\\d{4}-[01]\\d-[012]\\d";/*日期字符串的正则表达式  例如 2017-12-25*/
        mDataDayViewHelper=new DataDayViewHelper();
        mBaseDistance=mRadiusFill*3;
        mWarrningText=" I get nothing !";

        AdaptationParams();
        initParamsdpTopx();

    }

    private void initParamsdpTopx() {

        mLineLenngth=dip2px(mLineLenngth);
        mTextoffsetX=dip2px(mTextoffsetX);
        mTextSize =dip2px(mTextSize);
        mOffsetY=dip2px(mOffsetY);
        mLineStrokeWidth=dip2px(mLineStrokeWidth);

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

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(measureWidth < getMeasuredWidth()){
            measureWidth = getMeasuredWidth();
        }
        if(mLineLenngth<getMeasuredHeight()){
            mLineLenngth=getMeasuredHeight();
        }
        setMeasuredDimension(measureWidth, mLineLenngth);
        Log.i(TAG, "onMeasure: "+getWidth()+"------"+getHeight());
    }

    private void AdaptationParams() {
        int width=mBaseWidth;
        int mBaseHeight=1080;
        int height=0;
        try {
            Display display= ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            width=display.getWidth();
            height=display.getHeight();
            Log.i(TAG, "AdaptationParams:  "+width+" width height "+height);
        }catch (Exception e){
            Log.e(TAG, "onMeasure: ",e );
        }
       // mOffsetY=mOffsetY*width/mBaseWidth;
      //  mLineStrokeWidth=mLineStrokeWidth*width/mBaseWidth;//线条的粗细
        mOffsetRed=mOffsetRed*width/mBaseWidth;/*第一个实心圆在X方向的偏移量*/
        mOffsetGreen=mOffsetGreen*width/mBaseWidth;/*第二个实心圆在X方向的偏移量*/
        mRadiusFill=mRadiusFill*width/mBaseWidth; /*实心圆半径*/
       // mTextSize = mTextSize *width/mBaseWidth;/*文本的粗细*/
      //  mLineLenngth=height;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //统计
        int count1=0,count2=0;
        Date ipebDay = null;
        super.onDraw(canvas);
         /*whichLine数组记录每个数据所在的线条，只取 [0 ，mLineNumber ）为有效值，其他值看做无效
          * 无效值对应的数据不绘制
           * */
        int [] whichLine=null;
        try {
            //设置画笔
            initPaint();
            //画背景
            canvas.drawRect(0, 0, getWidth(), getHeight(), bg);
            //计算线条之间的距离
            int interval = mInterval * (getWidth() - getPaddingLeft() - getPaddingRight()) /(mInterval + mIntervalFrame) / mLineNumber;
            int intervalFrame = mIntervalFrame * (getWidth() - getPaddingLeft() - getPaddingRight()) / (mInterval + mIntervalFrame) / 2;

            mPaintLine.setColor(mLineColor);
            mLineLenngth=getHeight();

            /*计算绘制得线条的长度*/
            if(null!=mOffsetDataDay){
                Date startDay = mDataDayViewHelper.getOneDayFromString(mStartTime);
                whichLine=new int[mOffsetDataDay.length];
                int lenth=0;
                for(int i=0;i<mOffsetDataDay.length;i++){
                    ipebDay= mDataDayViewHelper.getOneDayFromString(mDataDay.get(i).getEvent_time());
                    //保存哪个事件画在哪条线
                    whichLine[i] = (int) ((ipebDay.getTime() - startDay.getTime()) / mDayMilliSecond);
                    if(whichLine[i]>mLineNumber-1||whichLine[i]<0){
                        count1++;
                        continue;
                    }
                    lenth= (int) (lenth+ mOffsetDataDay[i] * mBaseDistance);
                    count2++;
                }
                lenth=lenth+mOffsetY*2;/* 最后一个点到控件底部的距离*/
                mLineLenngth=Math.max(lenth, mLineLenngth);
            }

            /*绘制线条*/
            int startX = getPaddingLeft() + intervalFrame, startY = getPaddingTop();
            int stopX = startX, stopY = mLineLenngth + startY;
            canvas.drawLine(startX, startY, stopX, stopY, mPaintLine);
            for (int i = mLineNumber; i > 0; i--) {
              //  Log.i(TAG, "onDraw:startX+=interval " + startX);
                startX += interval;
                stopX += interval;
                canvas.drawLine(startX, startY, stopX, stopY, mPaintLine);
                if(mDataDay == null){
                    /*没有数据时进入此区域*/
                }
            }
            /*绘制各个数据点*/
            startX = getPaddingLeft() + intervalFrame;
            startY = getPaddingTop() + mOffsetY;
            mPaintLine.setStyle(Paint.Style.STROKE);
            mTextPaint.setTextSize(mTextSize);

            if (mDataDay != null) {
                int offsetIndex= -1;
                int x=0;
                int y = startY;
                for (IProbleEventBean ipeb : mDataDay) {
                    offsetIndex++;
                    if(whichLine[offsetIndex]>mLineNumber-1||whichLine[offsetIndex]<0){
                        continue;
                    }

                /*画笔类型 事件类型*/
                    int dIpeb=-1;
                    switch (ipeb.getKind()){
                        case EVENT_P:
                            dIpeb=EVENT_P_IC;
                            break;
                        case EVENT_C:
                            dIpeb=EVENT_C_IC;
                            break;
                        default:
                            dIpeb=-1;
                            break;
                    }
                    if(-1==dIpeb){
                        Log.i(TAG, "onDraw:其他事件不画  ");
                        continue;
                    }

                /*计算坐标*/
                    x = startX + whichLine[offsetIndex] * interval;
                    y = (int) (y + mOffsetDataDay[offsetIndex] * mBaseDistance);

                    Drawable drawable=getResources().getDrawable(dIpeb);
                    Bitmap bitmap= BitmapFactory.decodeResource(getResources(),dIpeb);
                    Rect oval = new Rect();/*确定绘图区域*/
                    oval.left = x-bitmap.getWidth()/3*1;
                    oval.top = y-bitmap.getHeight()/3*1;
                    oval.right =x+bitmap.getWidth()/3*2;
                    oval.bottom = y+bitmap.getHeight()/3*2;

                   drawable.setBounds(oval);
                    drawable.draw(canvas);

//                    显示时分秒
//                    canvas.drawText(ipeb.getEvent_time().substring(ipeb.getEvent_time().indexOf(' ') + 1
//                           ), mTriangleRectF.right - mRadiusFill, y, mTextPaint);
//                    显示时分
                    String str=ipeb.getEvent_time().substring(ipeb.getEvent_time().indexOf(' ') + 1,ipeb.getEvent_time().lastIndexOf(':'));
                    Rect textBounds=new Rect();
                    mTextPaint.getTextBounds(str,0,str.length(),textBounds);
                    canvas.drawText(str  ,mTextoffsetX+oval.right, oval.bottom-textBounds.height()/2, mTextPaint);

                }
                Log.i(TAG, "onDraw: 无效数据数目："+count1+" 有效数据数目："+count2);
            }
            canvas.restore();
        }catch(Exception e){
            Log.e(TAG, "onDraw: ",e );
        }

    }

    private void initPaint() {
        mPaintLine.setStrokeWidth(mLineStrokeWidth);
        bg.setColor(mBGColor);
    }

    /**
     * 设置View的数据
     * @param dataDayList 连续五天的数据量
     * @param startTime 起始日期 格式为yyyy-MM-dd，例如 2017-01-12
     * @param dataDayTextView 关联DataDayTextView控件的实例
     *
     */
    public void  setDataDay(final ArrayList<IProbleEventBean> dataDayList, final String startTime, DataDayTextView dataDayTextView  ) {
        try {
              /*关联控件*/
            mDataDayTextView = dataDayTextView;
            if (mDataDayTextView != null) {
                mDataDayTextView.setmInfo(new DataDayTextViewInfo(startTime, mInterval, mIntervalFrame, mLineNumber));
            }

            checkParamas(dataDayList, startTime);
//            if(thread!=null&&thread.getState()!=Thread.State.TERMINATED ){
//                thread.interrupt();
//                thread=null;
//            }
//            if(thread==null){
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //事件按时间排序
                            mDataDayViewHelper.sortEvents(dataDayList);
                            //获取时间间隔数组，不排序
                            int[] mEventIntervalTimeArray = mDataDayViewHelper.getEventIntervalTimeArray(dataDayList);
                            int[] mtempInt = mDataDayViewHelper.deleZeroFromArray(mEventIntervalTimeArray);/*去除0*/
                            int[] intervalTime = mDataDayViewHelper.getMinAndMax(mtempInt);
                            //计算偏移量,第一个为0
                            double[] offsetEvent = mDataDayViewHelper.calculateOffset(mEventIntervalTimeArray, intervalTime);
                            mDataDay = dataDayList;
                            mOffsetDataDay = offsetEvent;
                            mStartTime = startTime;
                            postInvalidate();
                        }catch (Exception e){
                            Log.d(TAG, "run: interrupt");
                            return ;
                        }
                    }
                });
                thread.start();
         //   }

        } catch (Exception e) {
            mDataDay = null;
            mOffsetDataDay = null;
            mStartTime = null;
            Log.e(TAG, "setDataDay: ", e);

        }finally {
               /* 更新控件*/
            invalidate();
        }
    }
        public Map<String,int[]> format(ArrayList<IProbleEventBean> dataDayList, String startTime) {
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


    /**
     * 检验参数是否合法
     * @param dataDayList 不为空
     * @param startTime 不为空，匹配模式"^\\d{4}-[01]\\d-[012]\\d"
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

    public  int dip2px(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;// 设备密度
        int px = (int) (dip * density + 0.5f);// 3.1->3, 3.9+0.5->4.4->4
        return px;
    }

    /**
     * 数据处理辅助类
     */
    static class  DataDayViewHelper{

        private String mDatePattern="yyyy-MM-dd" ;/*日期格式*/
        private String mPattern="yyyy-MM-dd HH:mm:ss" ;/*日期格式*/
        private SimpleDateFormat sdfOneDay,sdf;

        /**
         * 按事件时间排序
         * @param dataDayList
         */
        public void sortEvents(ArrayList<IProbleEventBean> dataDayList) {
            Collections.sort(dataDayList, new Comparator<IProbleEventBean>(){
                @Override
                public int compare(IProbleEventBean o1, IProbleEventBean o2) {
                    String time1=o1.getEvent_time(),time2=o2.getEvent_time();
                    time1=time1.substring(time1.indexOf(' ')+1);
                    time2=time2.substring(time2.indexOf(' ')+1);
                    return time1.compareTo(time2);
                }
            });
        }

        public int[] getEventIntervalTimeArray(ArrayList<IProbleEventBean> dataDayList) {
            int[] eventIntervalTimeArray=new int[dataDayList.size()-1];
            int temp=-1;
            int firstDayTime=-1;
            int secondDayTime=-1;
            int i=0;
            for(IProbleEventBean ipeb:dataDayList){
                if(firstDayTime==-1){
                    firstDayTime=getOneDaySecond(ipeb.getEvent_time());
                    continue;
                }else {
                    secondDayTime=getOneDaySecond(ipeb.getEvent_time());
                    temp=secondDayTime-firstDayTime;
                    eventIntervalTimeArray[i]=temp;
                    i++;
                    firstDayTime=secondDayTime;
                }
            }

            return eventIntervalTimeArray;
        }

        /**
         * 获取某时刻的秒数，例如1:00:00 为3600秒，2:00:00 为7200秒
         * @param event_time 字符格式为"yyyy-MM-dd HH:mm:ss"
         * @return HH:mm:ss 代表的秒数
         */
        private int getOneDaySecond(String event_time) {
            // TODO Auto-generated method stub
            return   getOneDaySecond_2(event_time);
        }

        private int getOneDaySecond_2(String event_time) {
            String str=event_time.substring(event_time.indexOf(' ')+1);
            String strs[]=str.split(":");
            int [] time=new int[]{
                    Integer.parseInt(strs[0]),
                    Integer.parseInt(strs[1]),
                    Integer.parseInt(strs[2]),
            };
            return time[0]*3600+time[1]*60+time[2];
        }

        /**
         * 获取日期，字符格式为"yyyy-MM-dd HH:mm:ss"
         * @param event_time
         * @return
         */
        private Date getDateFromString(String event_time) {
            Date date=null;
            if(sdf==null) {
                sdf = new SimpleDateFormat(mPattern);
            }
            try {
                date = sdf.parse(event_time);
            } catch (ParseException e) {
                Log.e(TAG, "getDateFromString: ",e );
            }
            return date;
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
         * 获取数组中的最大值和最小值
         * @param mEventIntervalTimeArray
         * @return 返回的数组只包含两个元素，第一位最小值，第二个为最大值
         */
        public int[] getMinAndMax(int[] mEventIntervalTimeArray) {
            int length = mEventIntervalTimeArray.length;
            if (mEventIntervalTimeArray == null) {
                return null;
            }
            if (length == 1) {
                return new int[]{mEventIntervalTimeArray[0], mEventIntervalTimeArray[0]};
            }
            if (length == 2) {
                return new int[]{
                        mEventIntervalTimeArray[0] < mEventIntervalTimeArray[1] ?
                                mEventIntervalTimeArray[0] : mEventIntervalTimeArray[1],
                        mEventIntervalTimeArray[0] > mEventIntervalTimeArray[1] ?
                                mEventIntervalTimeArray[0] : mEventIntervalTimeArray[1]
                };
            }
            int[] minAndMax = new int[2];
            minAndMax[0] = mEventIntervalTimeArray[0] < mEventIntervalTimeArray[1] ?
                    mEventIntervalTimeArray[0] : mEventIntervalTimeArray[1];
            minAndMax[1] = mEventIntervalTimeArray[0] > mEventIntervalTimeArray[1] ?
                    mEventIntervalTimeArray[0] : mEventIntervalTimeArray[1];
            for (int i = 2; i < length; i += 2) {
                if (i + 1 < length && mEventIntervalTimeArray[i] < mEventIntervalTimeArray[i + 1]) {
                    if (minAndMax[1] < mEventIntervalTimeArray[i + 1])//将较大值和nMax比较
                    {
                        minAndMax[1] = mEventIntervalTimeArray[i + 1];
                    }
                    if (minAndMax[0] > mEventIntervalTimeArray[i])//将较小值和nMin比较
                    {
                        minAndMax[0] = mEventIntervalTimeArray[i];
                    }
                } else if (i + 1 < length && mEventIntervalTimeArray[i] > mEventIntervalTimeArray[i + 1]) {
                    if (minAndMax[1] < mEventIntervalTimeArray[i])//将较大值和nMax比较
                    {
                        minAndMax[1] = mEventIntervalTimeArray[i];
                    }

                    if (minAndMax[0] > mEventIntervalTimeArray[i + 1])//将较小值和nMin比较
                    {
                        minAndMax[0] = mEventIntervalTimeArray[i + 1];
                    } else//最后剩下一个元素
                    {
                        if (minAndMax[1] < mEventIntervalTimeArray[i]) {
                            minAndMax[1] = mEventIntervalTimeArray[i];
                        }

                        if (minAndMax[0] > mEventIntervalTimeArray[i]) {
                            minAndMax[0] = mEventIntervalTimeArray[i];
                        }
                    }
                }
            }
            return minAndMax;
        }

        public int[] deleZeroFromArray(int[] mEventIntervalTimeArray) {
            ArrayList<Integer> al=new ArrayList();
            for (int i=0;i<mEventIntervalTimeArray.length;i++){
                if(mEventIntervalTimeArray[i]>0){
                    al.add(mEventIntervalTimeArray[i]);
                }
            }
            int [] Ints=new int[al.size()];
            for(int i=0;i<Ints.length;i++){
                Ints[i]=al.get(i);
            }
            return Ints;
        }

        public double[] calculateOffset(int[] mEventIntervalTimeArray,int[] maxAndmin) {
            int length=mEventIntervalTimeArray.length;
            double[] offset=new double[length+1];
            offset[0]=0;/*第一个事件的偏移量为0*/
            Log.i(TAG, "calculateOffset:maxAndmin "+maxAndmin[0]);
            try {
                for (int i = 1; i <= length; i++) {
                   // Log.i(TAG, "mEventIntervalTimeArray: "+mEventIntervalTimeArray[i-1]);
                    if(mEventIntervalTimeArray[i-1]==0){
                        offset[i]=0;
                        continue;
                    }
                    offset[i] = Math.log(mEventIntervalTimeArray[i-1] / maxAndmin[0]) + 1;
//                    Log.i(TAG, "calculateOffset: "+ offset[i]);
                }
            }catch (Exception e){
                Log.e(TAG, "calculateOffset: ",e );
            }
            return offset;
        }


    }


    public  static class DataDayTextViewInfo{
        String[] time;
        int[] info;
        private String mPattern="yyyy-MM-dd";
        private String pattern="^\\d{4}-[01]\\d-[012]\\d";
        private int mLineNumber;

        /**
         *
         * @param startTime 起始时间
         * @param interval 线条之间的间隔
         * @param intervalFrame 线条到控件的边缘的间隔
         * @param LineNumber 天数
         */
        public DataDayTextViewInfo(String startTime,int interval,int intervalFrame,int LineNumber){
            mLineNumber=LineNumber;
            setInfo(startTime,interval,intervalFrame,LineNumber);
        }

        public void setDays(String startTime){
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(startTime);
            if(!m.matches()){
                throw new IllegalArgumentException("startTime is not matche ,like yyyy-MM-dd ");
            }
            time=new String[mLineNumber];
            Date date=null;
            SimpleDateFormat sdfOneDay = new SimpleDateFormat(mPattern);
            Calendar calendar=Calendar.getInstance();
            try {
                date = sdfOneDay.parse(startTime);
                calendar.setTime(date);
                String str=null;
                for(int i=0;i<mLineNumber;i++){
                    str=sdfOneDay.format(date);
                    time[i]=str.substring(str.indexOf('-')+1);
                    //Log.i(TAG, "setDays: "+time[i]);
                    calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
                    date=calendar.getTime();   //这个时间就是日期往后推一天的结果
                }
            } catch (ParseException e) {
                Log.e(TAG, "getDateFromString: ",e );
            }

        }

        public void setInfo(String startTime,int interval,int intervalFrame,int LineNumber){
            setDays(startTime);
            info=new int[]{
                    LineNumber,interval,intervalFrame
            };

        }
    }
}
