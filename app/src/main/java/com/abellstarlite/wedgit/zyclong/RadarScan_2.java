package com.abellstarlite.wedgit.zyclong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.abellstarlite.wedgit.zyclong.utils.ImageUtils;
import com.zyclong.customview.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zyclong on 2017/3/23.
 */

public class RadarScan_2 extends View {
    private static final String TAG = "RadarScan";


    private int w, h;// 获取控件宽高
    private Paint mPaintLine;// 画雷达圆线
    private Paint mPaintSolid;// 画雷达渐变实心圆
    private Matrix matrix;


    private int[] mRadius;
    private volatile List<Bitmap> mBitmaps;
    private volatile List<int[]> mPositionList;
    private volatile List<String> mName;
    private int[] mIds;
    private Paint mPaintBitmap;
    private Bitmap mCenterBitmap;
    private int mStokeWidthBitmap = 0;
    private int maxPx;
    private int mCircleNumber;

    private Handler myHandler;
    private int maxNumber;
    private Bitmap mBaceBitmap;
    private OnRadarScanClickListener mOnClickListener;
    private boolean isClick = false;
    private int[] tmp;
    private float mBackX = 25;
    private float mBackY = 18;
    private long mLastTime = -1;
    private boolean isScan;
    private long mDelayedSacn = 150;
    private float mDegrees;
    private float mDegreeInterval = 4;
    private Thread task;
    private MyRunner taskRunner;

    private Bitmap[] bitmap;/*默认图标*/
    Rect b = new Rect();

    public RadarScan_2(Context context) {
        this(context, null);
    }

    public RadarScan_2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarScan_2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            isScan = true;
            mStokeWidthBitmap = dip2px(mStokeWidthBitmap);
            initPaint();
            setBackgroundResource(R.drawable.add_bg);//雷达的背景图片(紫色满天星，可以在微信APP中直接找到图片资源)
            mRadius = new int[]{/*只有三个使用，0 ，1 ，length-1*/
                    dip2px(40),/*z内圆半径*/
                    dip2px(60),/*各个圆半径间隔*/
                    dip2px(12),/*字体大小*/
                    dip2px(15)  /*设备头像的半径*/
            };
            myHandler = new ScanHandler();
            myHandler.sendEmptyMessageDelayed(0, mDelayedSacn);
            mBackX = dip2px(mBackX);
            mBackY = dip2px(mBackY);

        } catch (Exception e) {
            Log.e(TAG, "RadarScan: ", e);
        }
    }

    /*重绘时，旋转的角度*/
    private float rotation(float Degrees) {
        Degrees += mDegreeInterval;
        return Degrees % 360;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaintBitmap = new Paint();
        mPaintBitmap.setStrokeWidth(mStokeWidthBitmap);
        mPaintBitmap.setStyle(Paint.Style.STROKE);
        mPaintBitmap.setColor(Color.parseColor("#ffffff"));
        mPaintLine = new Paint();
        mPaintLine.setColor(Color.parseColor("#CCA1A1A1"));// 设置画笔
        mPaintLine.setStrokeWidth(dip2px(1));// 设置画笔宽度
        mPaintLine.setAntiAlias(true);// 消除锯齿
        mPaintLine.setStyle(Paint.Style.STROKE);// 设置空心

        mPaintSolid = new Paint();
        mPaintSolid.setAntiAlias(true);// 消除锯齿
        mPaintSolid.setStyle(Paint.Style.FILL);//实心圆
        matrix = new Matrix();//创建组件

        //四个空心圆半径,和出现设备的绘图半径

    }

    public int dip2px(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;// 设备密度
//        Log.i(TAG, "dip2px: "+density);
        int px = (int) (dip * density + 0.5f);// 3.1->3, 3.9+0.5->4.4->4
        return px;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        w = getWidth();
        h = getHeight();
        /**
         * 处理动画抖动问题
         */
        if (isScan) {
            int i = (int) ((System.currentTimeMillis() - mLastTime) / (mDelayedSacn / mDegreeInterval));
            i = (int) Math.min(i, mDegreeInterval);
            mDegrees += i;
            matrix.postRotate(mDegrees, getWidth() / 2, getHeight() / 2);//旋转矩阵
        }


        maxPx = w > h ? w : h;
        mCircleNumber = (maxPx / 2 - mRadius[0]) / mRadius[1];
        maxNumber = w / mRadius[mRadius.length - 1] / 4;
        maxNumber = maxNumber * h / mRadius[mRadius.length - 1] / 4;
        maxNumber *= 0.7;
        try {
            //空心圆
            for (int i = 0; i <= mCircleNumber; i++) {
                canvas.drawCircle(w / 2, h / 2, mRadius[0] + mRadius[1] * i, mPaintLine);
            }

            if (mBaceBitmap == null) {
                mBaceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_back);
            }
            canvas.drawBitmap(mBaceBitmap,
                    mBackX,
                    mBackY,
                    mPaintBitmap);
            //渐变
            Shader mShader = new SweepGradient(w / 2, h / 2,
                    new int[]{
                            Color.TRANSPARENT,
                            Color.TRANSPARENT,
                            Color.parseColor("#33FFFFFF")

                    },
                    null);
            mPaintSolid.setShader(mShader);
            canvas.setMatrix(matrix);
            canvas.drawCircle(w / 2, h / 2, mCircleNumber * mRadius[1] + mRadius[0], mPaintSolid);
            matrix.reset();//重置矩阵，避免累加，越转越快'
            canvas.setMatrix(null);

        /*画中心图片*/
            if (mCenterBitmap == null) {
                mCenterBitmap = ImageUtils.RoundBitmap(getResources(),
                        R.drawable.add_bg,
                        mRadius[0] * 2 - mStokeWidthBitmap * 2);
            }

            canvas.drawBitmap(mCenterBitmap,
                    (float) (w / 2 - mRadius[0] + mStokeWidthBitmap),
                    (float) (h / 2 - mRadius[0] + mStokeWidthBitmap),
                    mPaintBitmap);

            mPaintBitmap.setTextSize((float) (mRadius[mRadius.length - 2]));
            /*画设备头像*/
            if (mBitmaps != null && mPositionList != null) {
                for (int j = 0; j < mPositionList.size(); j++) {
                    //  canvas.drawCircle(mPositionList.get(j)[0], mPositionList.get(j)[1],mRadius[mRadius.length-1]-mStokeWidthBitmap/2, mPaintBitmap);
                    canvas.drawBitmap(mBitmaps.get(j),
                            (float) (mPositionList.get(j)[0] - mRadius[mRadius.length - 1] + mStokeWidthBitmap),
                            (float) (mPositionList.get(j)[1] - mRadius[mRadius.length - 1] + mStokeWidthBitmap),
                            mPaintBitmap);
                    mPaintBitmap.getTextBounds(mName.get(j), 0, mName.get(j).length(), b);
                    canvas.drawText(mName.get(j),
                            mPositionList.get(j)[0] - b.width() / 2,
                            mPositionList.get(j)[1] + b.height() + mRadius[mRadius.length - 1] + dip2px(4),
                            mPaintBitmap);
                }
//                Log.i(TAG, "onDraw: size" + mPositionList.size());
            }

            super.onDraw(canvas);
        } catch (Exception e) {
            Log.e(TAG, "onDraw: ", e);
        }
    }

    private void getPosition(int length) {
        if (mPositionList == null) {
            mPositionList = new ArrayList();
        }
        for (int i = 0; i < length; i++) {
            int[] position;
            do {
                position = getXY();
            } while (!checkPosition(position));
            mPositionList.add(position);
        }

    }

    /**
     * 新添加的圆不能与已有的圆重合
     * 计算两个圆的距离，大于mRadius[mRadius.length-1]返回true
     */
    private boolean checkPosition(int[] position) {
        if (mPositionList == null) {
            return true;
        }
        if (position == null) {
            return false;
        }
        /*圆形区域为屏幕可视范围*/
        if (position[0] > w - mRadius[mRadius.length - 1]
                || position[1] > h - mRadius[mRadius.length - 1]
                || position[0] < mRadius[mRadius.length - 1]
                || position[1] < mRadius[mRadius.length - 1]) {
            return false;
        }

        if (mPositionList.size() > maxNumber) {
            return true;
        }
        for (int[] p : mPositionList) {
            float distance = (float) Math.sqrt((p[0] - position[0]) * (p[0] - position[0]) + (p[1] - position[1]) * (p[1] - position[1]));
            if (distance < 2 * mRadius[mRadius.length - 1]) {
                return false;
            }
        }
        return true;
    }

    private int[] getXY() {
        int count = 0;
        int rX, rY;
        while (mCircleNumber == 0) {
            try {
                count++;
                Thread.sleep(100);
                if (count > 100) {
                    throw new IllegalArgumentException("getXY：控件初始化错误");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int radom = (int) (Math.rint(Math.random() * (mCircleNumber - 2)) + 1);
        int r = mRadius[0] + mRadius[1] * radom;/*半径*/
        rX = (int) (Math.random() * r);
        rY = (int) Math.sqrt(Math.abs(r * r - rX * rX));
        radom = (int) (Math.rint(Math.random() * 4));
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
        return new int[]{rX, rY};
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // return super.onTouchEvent(event);
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int which = getWhich(event.getX(), event.getY());
                if (which == -1) {
                    isClick = false;
                } else {
                    isClick = true;
                    if (tmp == null) {
                        tmp = new int[3];
                    }
                    tmp[0] = (int) event.getX();
                    tmp[1] = (int) event.getY();
                    tmp[2] = which;
                }
                break;
            case MotionEvent.ACTION_MOVE:


                break;
            case MotionEvent.ACTION_UP:
                if (isClick) {
                    if (event.getX() - tmp[0] < mRadius[mRadius.length - 1]) {
                        if (event.getY() - tmp[1] < mRadius[mRadius.length - 1]) {
                            if (mOnClickListener != null) {
                                if (tmp[2] == -2) {
                                    mOnClickListener.onClickBack();
                                } else {
                                    mOnClickListener.onClick(mName.get(tmp[2]));
                                }
                            }
                        }
                    }
                }
                break;
        }

        return true;
    }

    public void setOnClickListener(OnRadarScanClickListener l) {
        mOnClickListener = l;
    }

    private int getWhich(float x, float y) {
        if (x > mBackX && x < mBackX + mBaceBitmap.getWidth() && y > mBackY && y < mBackY + mBaceBitmap.getHeight()) {
            return -2;
        }
        if (mPositionList == null || mPositionList.size() == 0) {
            return -1;
        }
        int xi, yi;
        for (int i = mPositionList.size() - 1; i >= 0; i--) {
            xi = mPositionList.get(i)[0];
            yi = mPositionList.get(i)[1];
            if (Math.abs(x - xi) < mRadius[mRadius.length - 1]) {
                if (Math.abs(y - yi) < mRadius[mRadius.length - 1]) {
                    //    Log.i(TAG, "getWhich: "+mName.get(i));
                    return i;
                }
            }
        }
        return -1;
    }

    public Bitmap getmCenterBitmap() {
        return mCenterBitmap;
    }


    /**
     * 每次调用此方法后RadarScanAdapter的数据都会被清除，必须再次设置数据才会生效
     * 例如：
     *          RadarScanAdapter mRadarScanAdapter =new RadarScanAdapter();
     *          mRadarScanAdapter.setDataList(data);
     *          mRadarScan.setAdapter(mRadarScanAdapter, false);//成功后会清楚数据
     *          mRadarScan.setAdapter(mRadarScanAdapter, false);//此次设置数据为空
     *          等价于：
     *           RadarScanAdapter mRadarScanAdapter =new RadarScanAdapter();
     *          mRadarScanAdapter.setDataList(data);
     *          mRadarScan.setAdapter(mRadarScanAdapter, false);
     *          mRadarScan.setAdapter(null, false);
     * @param rsa
     * @param isRefresh 是否已有清除数据
     */
    public synchronized void setAdapter(RadarScanAdapter rsa, boolean isRefresh) {
        taskRunner = new MyRunner() {
            Thread last = null;
            RadarScanAdapter rsa;
            Boolean isRefresh;
            @Override
            public void setParams(RadarScanAdapter rsa, boolean isRefresh, Thread last) {
                try {
                    this.rsa = rsa.clone();
                    this.isRefresh = isRefresh;
                    this.last = last;
                    rsa.clear();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void run() {
                int length = 0;
                if (last != null) {
                    try {
                        last.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                synchronized (RadarScan_2.class) {
                    try {
                        if (mBitmaps == null) {
                            mBitmaps = new ArrayList<>();
                        }
                        if (mName == null) {
                            mName = new ArrayList<>();
                        }
                        if (isRefresh) {
                            mBitmaps.clear();
                            mName.clear();
                            if(mPositionList==null){
                                mPositionList=new ArrayList<>();
                            }
                            mPositionList.clear();
                        }

                        if (rsa == null) {
                            return;
                        }
                        if (rsa.Icondp != -1) {
                            mRadius[mRadius.length - 1] = dip2px(rsa.Icondp);
                        }
                        if (rsa.textsize != -1) {
                            mRadius[mRadius.length - 2] = dip2px(rsa.textsize);
                        }
                        if (rsa.IconCenterdp != -1) {
                            mRadius[0] = dip2px(rsa.IconCenterdp);
                        }
                        if (rsa.interval != -1) {
                            mRadius[1] = dip2px(rsa.interval);
                        }
                        if (rsa.IconCenterBitmap != null) {
                            mCenterBitmap = rsa.IconCenterBitmap;
                        }
                        if (rsa.IconCenter != -1) {
                            mCenterBitmap = ImageUtils.RoundBitmap(getResources(),
                                    rsa.IconCenter,
                                    mRadius[0] * 2 - mStokeWidthBitmap * 2);
                        }
                        String str;
                        int i = 0;

                        if (rsa.dataBitmap != null) {
                            Iterator iterator = rsa.dataBitmap.keySet().iterator();
                            while (iterator.hasNext()) {
                                str = (String) iterator.next();
                                if(mName.contains(str)){
                                    continue;
                                }
                                mName.add(str);
                                length++;
                                mBitmaps.add(ImageUtils.zoomImg(rsa.dataBitmap.get(str),
                                        mRadius[mRadius.length - 1] * 2 - mStokeWidthBitmap * 2,
                                        mRadius[mRadius.length - 1] * 2 - mStokeWidthBitmap * 2));
                            }
                        }
                        /*设备类别*/
                        if (rsa.dataListMap != null) {
                            if (bitmap == null) {
                                bitmap = new Bitmap[DeviceCategory.getDeviceCategoryNumber()];
                                for (DeviceCategory device : DeviceCategory.values()) {
                                    bitmap[device.position] = ImageUtils.zoomBitmap(getResources(),
                                            device.getId(),
                                            mRadius[mRadius.length - 1] * 2 - mStokeWidthBitmap * 2);
                                }
                            }
                            DeviceCategory deviceCategory;
                            Iterator iterator = rsa.dataListMap.keySet().iterator();
                            while (iterator.hasNext()) {
                                deviceCategory = (DeviceCategory) iterator.next();
                                List<String> list = rsa.dataListMap.get(deviceCategory);
                                for (String Str : list) {
                                    if(mName.contains(Str)){
                                        continue;
                                    }
                                    mName.add(Str);
                                    length++;
                                    mBitmaps.add(bitmap[deviceCategory.position]);
                                }

                            }
                        }

                        if (rsa.dataList != null) {
                            if (bitmap == null) {
                                bitmap = new Bitmap[DeviceCategory.getDeviceCategoryNumber()];
                                for (DeviceCategory device : DeviceCategory.values()) {
                                    bitmap[device.position] = ImageUtils.zoomBitmap(getResources(), device.getId(), mRadius[mRadius.length - 1] * 2 - mStokeWidthBitmap * 2);
                                }
                            }
                            Iterator iterator = rsa.dataList.iterator();
                            while (iterator.hasNext()) {
                                str = (String) iterator.next();
                                if(mName.contains(str)){
                                    continue;
                                }
                                mName.add(str);
                                length++;
                                mBitmaps.add(bitmap[DeviceCategory.DIAPER.position]);
                            }
                        }
                        if (rsa.data != null) {
                            if (mIds == null) {
                                mIds = new int[rsa.data.size()];
                            }
                            Iterator iterator = rsa.data.keySet().iterator();
                            while (iterator.hasNext()) {
                                str = (String) iterator.next();
                                mName.add(str);
                                length++;
                                mIds[i] = rsa.data.get(str);
                                i++;
                            }
                        }
                        rsa.clear();
                        addBitmapInRadarScan(mIds, length);
                        mIds = null;
                    } catch (Exception e) {
                        Log.e(TAG, "setAdapter: ", e);
                    }
                }
            }
        };
        Thread last = task;
        taskRunner.setParams(rsa, isRefresh, last);
        task = new Thread(taskRunner);
        task.start();
    }


    /**
     * 设置中心头像
     *
     * @param resId
     */
    public void setCenterBitmap(@DrawableRes int resId) {
        mCenterBitmap = ImageUtils.RoundBitmap(getResources(), resId, mRadius[0] * 2 - mStokeWidthBitmap * 2);
        invalidate();
    }

    /**
     * 设置中心头像
     *
     * @param background 要求为 BitmapDrawable
     */
    public void setCenterDrawable(BitmapDrawable background) {
        mCenterBitmap = ImageUtils.RoundBitmap(background, mRadius[0] * 2 - mStokeWidthBitmap * 2);
        invalidate();
    }

    /**
     * 添加雷达要显示的图片
     *
     * @param bitmaps
     */
    private void addBitmapInRadarScan(@DrawableRes final int[] bitmaps, final int length) {
        if (mBitmaps == null) {
            mBitmaps = new ArrayList<>();
        }
        try {
            if (bitmaps != null) {
                for (int i = 0; i < bitmaps.length; i++) {
                    mBitmaps.add(ImageUtils.zoomBitmap(getResources(),
                            bitmaps[i],
                            mRadius[mRadius.length - 1] * 2 - mStokeWidthBitmap * 2));
                }
            }
            getPosition(length);
            myHandler.sendEmptyMessage(1);
        } catch (Exception e) {
            Log.e(TAG, "run: ", e);
        }
    }

    /**
     * 扫描动画的开关
     *
     * @param isScan true为打开动画
     */
    public void setAnimation(boolean isScan) {
        this.isScan = isScan;
        myHandler.sendEmptyMessage(0);
    }

    public interface OnRadarScanClickListener {

        /**
         * dataKey 点击位置所在的Bitmap对应的key
         * RadarScanAdapted.data.keyset()的key
         * 或者是dataKey RadarScanAdapted.dataBitmap.keyset()的key
         *
         * @param dataKey
         */
        void onClick(String dataKey);

        void onClickBack();
    }

    public static class RadarScanAdapter implements Cloneable {
        private Map<String, Integer> data;
        private Map<String, Bitmap> dataBitmap;
        private Map<DeviceCategory, List<String>> dataListMap;
        private int Icondp;
        private int interval;
        private int textsize;
        private int IconCenterdp;
        private int IconCenter;
        private Bitmap IconCenterBitmap;
        private List<String> dataList;


        public RadarScanAdapter() {
            clear();
        }

        /**
         * 设置分别类别数据，采用默认图标
         *
         * @param data
         */
        public void setDataListMap(Map<DeviceCategory, List<String>> data) {
            this.dataListMap = data;
        }

        /**
         * 设置数据，采用默认图标
         *
         * @param dataList
         */
        public void setDataList(List<String> dataList) {
            this.dataList = dataList;
        }


        /**
         * 设置雷达要描绘的图标
         *
         * @param dataBitmap Map<String, Bitmap> String对应Bitmap
         */
        public void setDataBitmap(Map<String, Bitmap> dataBitmap) {
            this.dataBitmap = dataBitmap;
        }

        /**
         * 设置雷达要描绘的图标
         *
         * @param data Map<String, Integer> String对应Integer，Integer为@DrawableRes 图标资源id
         */
        public void setData(Map<String, Integer> data) {
            this.data = data;
        }

        /**
         * 设置设置雷达要描绘的图标半径，单位dp
         *
         * @param icondp
         */
        public void setIcondp(int icondp) {
            Icondp = icondp;
        }

        /**
         * 设置每个圆的间隔，单位dp
         *
         * @param interval
         */
        public void setInterval(int interval) {
            this.interval = interval;
        }

        /**
         * 设置设置雷达中心位置图标半径，单位dp
         *
         * @param iconCenterdp
         */
        public void setIconCenterdp(int iconCenterdp) {
            IconCenterdp = iconCenterdp;
        }

        /**
         * 设置设置字体大小半径，单位dp
         *
         * @param textsize
         */
        public void setTextsize(int textsize) {
            this.textsize = textsize;
        }

        /**
         * 设置设置雷达中心位置图标
         *
         * @param iconCenter
         */
        public void setIconCenter(@DrawableRes int iconCenter) {
            IconCenter = iconCenter;
        }

        /**
         * 设置设置雷达中心位置图标
         *
         * @param iconCenterBitmap
         */
        public void setIconCenterBitmap(Bitmap iconCenterBitmap) {
            IconCenterBitmap = iconCenterBitmap;
        }

        public void clear() {
            data = null;
            dataBitmap = null;
            dataList = null;
            IconCenterBitmap = null;
            dataListMap = null;
            Icondp = -1;
            interval = -1;
            IconCenterdp = -1;
            IconCenter = -1;
            textsize = -1;
        }

        @Override
        public RadarScanAdapter clone() throws CloneNotSupportedException {
            return (RadarScanAdapter) super.clone();
        }
    }

    private class ScanHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != 0) {
                invalidate();
                return;
            }
            mDegrees = rotation(mDegrees);
            mLastTime = System.currentTimeMillis();
            if (isScan) {
                invalidate();
                myHandler.sendEmptyMessageDelayed(0, mDelayedSacn);
            }
        }
    }

    public enum DeviceCategory {
        //  CAMARE(1),
        DIAPER(0);

        DeviceCategory(int position) {
            this.position = position;
        }

        private static int[] mIconId = new int[]{  /*设备头像*/
                R.drawable.icon_add_device /*尿布扣*/
        };
        private int position;

        public int getId() {
            return mIconId[this.position];
        }

        public static int getDeviceCategoryNumber() {
            return mIconId.length;
        }

    }

    public interface MyRunner extends Runnable {
        void setParams(RadarScanAdapter rsa, boolean isRefresh, Thread last);
    }
}
