package com.abellstarlite.wedgit.zyclong;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyclong.customview.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by zyclong on 2017/3/28.
 */

public class RadarScan extends RelativeLayout implements View.OnClickListener {
    public static final String TAG = "RadarScan";
    private Drawable mDrawable;
    private volatile List<String> mName;
    private volatile List<View> mViews;
    private Handler mHandler;
    private Calcullate mCalcullate;
    private volatile List<int[]> mPositionList;
    int w, h;
    private Activity mActivity;
    private int viewWidth;
    private int viewHeight;
    private int viewMinHeight;
    private int viewMinWidth;
    private volatile int length;
    private float scale = 1.5f;
    private int mTextColor = Color.WHITE;
    private int maxNum;
    private long mDelayMills = 27;
    private long mDelayMillsSleep = 60 * 60*1000;
    private long mDelayMills1 = mDelayMills * 15;
    private long mDelayMills2 = mDelayMills * 13;
    private RadarScan mRadarScan_;
    private boolean isExit = false;
    Thread task;
    int len;
    int size;
    Object obj = new Object();
    Runnable myRunnable;
    private CallBack mCallBack;
    private Rect mRect;
    Paint mPaint;
    private float mTextSize = 10;
    private float mCenterdp = 70;
    private float mImterval = 20;
    private float mImtervalh = 20;
    private float mSize;


    public int getViewMinWidth() {
        return viewMinWidth;
    }


    public int getViewMinHeight() {
        return viewMinHeight;
    }


    public RadarScan(Context context) {
        this(context, null);
    }

    public RadarScan(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarScan(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams();
        mHandler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                try {
                    if (msg.what == -1) {
                        addView();
                        return;
                    }
                    if (task == null) {
                        isExit = true;
                        task = new Thread(myRunnable);
                        task.start();
                        return;
                    }
                    if (!task.isAlive()) {
                        if (task.getState() == Thread.State.TERMINATED) {
                            isExit = true;
                            task = new Thread(myRunnable);
                            task.start();
                        }
                    } else {
                        if (task.getState() == Thread.State.TIMED_WAITING) {
                            task.interrupt();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "handleMessage: ", e);
                }
            }
        };
    }

    private void initParams() {
        mSize=35;
        mCalcullate = new CalcullateIml();
        mPaint = new Paint();
        mPaint.setTextSize(sp2px(mTextSize));
        viewMinWidth = dip2px(40);
        viewMinHeight = dip2px(50);
        mRadarScan_ = this;
        length = 0;
        myRunnable = new Runnable() {
            boolean send;

            @Override
            public void run() {
                while (isExit) {
                    try {
                        while (length > 0) {
                            mCalcullate.addPosition(w, h);
                            //  Log.i(TAG, "run: sleep" + mPositionList.size());
                            synchronized (obj) {
                                length--;
                                send = true;
                            }
                        }
                        if (send) {
                            mHandler.sendEmptyMessage(-1);
                            send = false;
                        }
                        //    Log.i(TAG, "run: sleep" + mPositionList.size());
                        Thread.sleep(mDelayMillsSleep);
                    } catch (InterruptedException e) {

                        // Log.i(TAG, "run: alive");
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        };
     //   setViewWidth("....");

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        setMeasuredDimension(display.getWidth(), display.getHeight());


    }

    public int dip2px(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;// 设备密度
//        Log.i(TAG, "dip2px: "+density);
        int px = (int) (dip * density + 0.5f);// 3.1->3, 3.9+0.5->4.4->4
        return px;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    @Override
    public void addView(View child) {

        super.addView(child);
    }

    public void addView() {
        try {
            // super.addView(child);
            if (mActivity == null) {
                return;
            }

            String str;
            int[] xy;
            View v = null;
            RelativeLayout.LayoutParams lp;
            TextView vTv;
            Log.i(TAG, "addView: " + mPositionList.size());
            synchronized (obj) {
                size = mViews.size();
                for (int i = 0; i < len; i++) {
                    if (size + i >= mPositionList.size()) {
                        return;
                    }
                    xy = mPositionList.get(size + i);
                    str = mName.get(size + i);
                    v = mActivity.getLayoutInflater().inflate(R.layout.radarscan_device, null);
                    v.setOnClickListener(mRadarScan_);
                    vTv = (TextView) v.findViewById(R.id.tv_device);
                    vTv.setText(str);
                    mTextSize = vTv.getTextSize();
                    vTv.setTextColor(mTextColor);
                    v.setTag(str);
                    v.setClickable(true);
                    mViews.add(v);

                    lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    lp.leftMargin = xy[0];
                    lp.topMargin = xy[1];

                    final View finalV = v;
                    final RelativeLayout.LayoutParams lp1 = lp;
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addView(finalV, lp1);
                        }
                    }, mDelayMills1);

                }
            }
            mHandler.sendEmptyMessageDelayed(-1, mDelayMills2);
        } catch (Exception e) {
            Log.e(TAG, "addView: ", e);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        maxNum = w * h / getViewWidth() / getViewHeight() / 3;
        len = maxNum / 2;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void addDataList(List<String> dataList, boolean isRefresh, Activity mActivity, CallBack callBack) {
        try {
            this.mActivity = mActivity;
            mCallBack = callBack;

            if (mName == null) {
                mName = new ArrayList<>();
            }
            if (mViews == null) {
                mViews = new ArrayList<>();
            }
            if (mPositionList == null) {
                mPositionList = new ArrayList<>();
            }

            if (dataList == null || dataList.size() == 0) {
             //   mHandler.sendEmptyMessage(0);
                return;
            }
            // mPositionList.clear();
            if (isRefresh) {
                if (task != null) {
                    exitRadarScan();
                    Log.i(TAG, "Refresh data");
                }
                synchronized (obj) {
                    mViews.clear();
                    mName.clear();
                    mPositionList.clear();
                }
                removeAllViews();
            }
            Iterator iterator = dataList.iterator();
            String str;
            synchronized (obj) {
                while (iterator.hasNext()) {
                    str = (String) iterator.next();
                    if (mName.contains(str)) {
                        continue;
                    }
                    length++;
                    mName.add(str);
                }
            }
            View v;
            TextView vTv;
            v = mActivity.getLayoutInflater().inflate(R.layout.radarscan_device, null);
            vTv = (TextView) v.findViewById(R.id.tv_device);
            mTextSize = Math.max(mTextSize, vTv.getTextSize());
            if (mName.size() > 0) {
                setViewWidth(mName.get(0));
            }
            maxNum = w * h / getViewWidth() / getViewHeight() / 3;
            len = maxNum / 2;
            Log.i(TAG, "addDataList:mName " + getWidth() + "-----" + isRefresh + "----" + dataList.size());
            mHandler.sendEmptyMessageDelayed(length, 1000);
        } catch (Exception e) {
            Log.e(TAG, "addDataList: ", e);
        }
    }


    @Override
    public void onClick(View v) {
        if (mCallBack != null) {
            mCallBack.callback(v.getTag().toString());
        }
    }


    public int setViewWidth(String str) {
        if (mRect == null) {
            mRect = new Rect();
        }

        String str1 = str + "...";
        mPaint.getTextBounds(str1, 0, str1.length(), mRect);
        // float le= mPaint.measureText(str);
        viewWidth = Math.max(mRect.width(), dip2px(50));
        setViewHeight(mRect.height());
        return viewWidth;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        viewHeight = dip2px(50);
        return viewHeight;
    }

    public void setViewHeight(int viewHeight) {
        viewHeight = Math.max(viewHeight, dip2px(mSize));
        this.viewHeight = viewHeight;
    }

    /**
     * 退出扫描，在退出时使用该方法
     */
    public void exitRadarScan() {
        if (task != null && task.isAlive()) {
            synchronized (obj) {
                isExit = false;
                task.interrupt();
                try {
                    mHandler.sendEmptyMessage(-1);
                    if(length>0&&mName.size()>=length){
                        for (int i = length-1; i > 0; i--) {
                            mName.remove(i);
                        }
                    }

                    length = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                    // Log.i(TAG, "run: alive");
                }
            }
        }

    }

    public interface Calcullate {
        void addPosition(int w, int h);
    }

    public class CalcullateIml implements Calcullate {
        int w;
        int h;

        @Override
        public void addPosition(int w, int h) {
            this.w = w;
            this.h = h;
            int[] xy = null;
            do {
                xy = getPosition();
            } while (check(xy));
            mPositionList.add(xy);
        }

        private int[] getPosition() {
            return getXY();
        }

        private boolean check(int[] xy) {
            if (xy[0] + getViewWidth() > w || xy[0] < 0) {
                return true;
            }
            if (xy[0] + viewMinWidth > w || xy[0] < 0) {
                return true;
            }

            if (xy[1] + viewMinHeight + dip2px(mImtervalh) > h || xy[1] < 0) {
                return true;
            }
            if (xy[0] < dip2px(80) && xy[1] < dip2px(60)) {
                return true;
            }

//            if(xy[0] < dip2px(mCenterdp)+w/2 &&xy[0] >w/2-dip2px(mCenterdp) ){
//                if(xy[1] < dip2px(mCenterdp)+h/2 &&xy[1] >h/2-dip2px(mCenterdp) ) {
//                    return true;
//                }
//            }
            Log.i(TAG, mPositionList.size() + "check: " + maxNum);
            if (mPositionList.size() >= maxNum) {
                return false;
            }

            for (int[] x : mPositionList) {
//                float distance = (float) Math.sqrt((x[0] - xy[0]) * (x[0] - xy[0]) + (x[1] - xy[1]) * (x[1] - xy[1]));

//                if (distance < getDistance()) {
//                    return true;
//                }
                int xx = Math.abs(x[0] - xy[0]);
                int xyy = Math.abs(x[1] - xy[1]);
                Log.i(TAG, "check:  " + xx + "---yy" + xyy + "--" + getViewWidth() + "--" + getViewHeight());
                if (xx < getViewWidth()) {
                    if (xyy < getViewHeight()) {
                        return true;
                    }
                }


            }
            return false;
        }


        private int[] getXY() {
            int rX, rY;
            int num = 10;

            int radom = (int) (Math.rint(Math.random() * num) + 2);
            int r = getRaduis(radom);
            rX = (int) (Math.random() * r);
            rY = (int) Math.sqrt(Math.abs(r * r - rX * rX));
            radom = (int) (Math.rint(Math.random() * 3));
            if (radom == 0) {
                rX = w / 2 - rX;
                rY = h / 2 - rY;
            } else if (radom == 1) {
                rX = w / 2 + rX;
                rY = h / 2 - rY;
            } else if (radom == 2) {
                rX = w / 2 + rX;
                rY = h / 2 + rY;
            } else {
                rX = w / 2 - rX;
                rY = h / 2 + rY;
            }

            rX = rX - dip2px(20);
            rY = rY - dip2px(20
            );
            return new int[]{rX, rY};
        }

        private int getRaduis(int radom) {
            int r = dip2px(mCenterdp);
            return r + radom * dip2px(mImterval);
        }


    }

    private float getDistance() {
        return (float) (Math.max(getViewWidth(), getViewHeight()));
    }

    public interface CallBack {
        void callback(String name);
    }

}
//long.customview I/RadarScan: 2check: 17
//        04-06 02:25:37.680 14989-15168/com.zyclong.customview I/RadarScan: check:  106---yy479--376--150
//        04-06 02:25:37.680 14989-15168/com.zyclong.customview I/RadarScan: 2check: 17
//        04-06 02:25:37.680 14989-15168/com.zyclong.customview I/RadarScan: check:  260---yy831--376--150
//        04-06 02:25:37.680 14989-15168/com.zyclong.customview I/RadarScan: check:  617---yy107--376--150
//        04-06 02:25:37.680 14989-15168/com.zyclong.customview I/RadarScan: check:  343---yy690--376--150
//        04-06 02:25:37.680 14989-15168/com.zyclong.customview I/RadarScan: 2check: 17
//        04-06 02:25:37.680 14989-15168/com.zyclong.customview I/RadarScan: check:  176---yy484--376--150
//        04-06 02:25:37.680 14989-15168/com.zyclong.customview I/RadarScan: check:  13---yy867--376--150
