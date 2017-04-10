package com.zyclong.customview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.abellstarlite.wedgit.zyclong.RadarScan_2;
import com.abellstarlite.wedgit.zyclong.RadarScan;
import com.abellstarlite.wedgit.zyclong.service.NotificationService;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;


public class Main6Activity extends AppCompatActivity {
    public static final String TAG ="Main6Activity" ;

    RadarScan_2 mRadarScan2;
    Handler mh;
    ImageView imageView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    boolean f=true;
    private Runnable r;
    FrameLayout frameLayout;
    RelativeLayout mRelativeLayout;
    RadarScan_2.RadarScanAdapter mRadarScanAdapter;
    RadarScan mRadarScan_;
    private RadarScan.CallBack cb;
    final List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            //无title
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_main6);

          //  mRelativeLayout= (RelativeLayout) findViewById(R.id.RelativeLayout);
            frameLayout= (FrameLayout) findViewById(R.id.activity_main6);
            imageView= (ImageView) findViewById(R.id.RadarScan_1);
            mRadarScan_ = (RadarScan) findViewById(R.id.RadarScan_);
            final Animation circle_anim = AnimationUtils.loadAnimation(this, R.anim.radarscan_anim);
          //  mRadarScan_1.addDataList(null,false);
            LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
            circle_anim.setInterpolator(interpolator);
            imageView.startAnimation(circle_anim);

//            LayoutInflater inflater = LayoutInflater.from(Main6Activity.this);
            LayoutInflater inflater = getLayoutInflater();
           View v= inflater.inflate(R.layout.radarscan_device,null);
            RelativeLayout.LayoutParams  lp=new RelativeLayout.LayoutParams (dip2px(150),dip2px(50));

            lp.topMargin=100;
            lp.leftMargin=100;
            v.setLayoutParams(lp);
            ImageView vv= (ImageView) v.findViewById(R.id.iv_device);
            vv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onClick: view");
                }
            });
        //    mRelativeLayout.addView(v,lp);
       //     mRadarScan_1.addView(v,lp);

         //   mRadarScan_1.startAnimation(circle_anim);
//            mRadarScan = (RadarScan) findViewById(R.id.RadarScan);
//            mRadarScan.setOnClickListener(new RadarScan.OnRadarScanClickListener() {
//                @Override
//                public void onClick(String dataKey) {
//                    Snackbar.make(mRadarScan, "onclick " + dataKey, Snackbar.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onClickBack() {
//                    Snackbar.make(mRadarScan, "onclick back", Snackbar.LENGTH_LONG).show();
//                }
//            });
//
//            final Map<String, Integer> data1 = new HashMap<>();

            for (int i = 0; i < 13; i++) {
                data.add((int)(Math.random()*9)
                        +""+(int)(Math.random()*9)
                        +""+(int)(Math.random()*9)
                        +""+(int)(Math.random()*9)
                        +""+(int)(Math.random()*9)
                        +""+(int)(Math.random()*9)
                        +""+(int)(Math.random()*9)
                        +""+(int)(Math.random()*9)
                        +""+(int)(Math.random()*9)
                        +""+(int)(Math.random()*9)
                        +""+(int)(Math.random()*9)
                        +""+(int)(Math.random()*9)
                        +""+(int)(Math.random()*9)
                        +""+(int)(Math.random()*9)
                        +""+(int)(Math.random()*9)
                        +""+(int)(Math.random()*9)
                        +""+(int)(Math.random()*9)

                       );
            }
            cb=new RadarScan.CallBack() {
                @Override
                public void callback(String name) {
                    Log.i(TAG, "callback: "+name);
                }
            };
            mRadarScan_.addDataList(null, false, this,cb) ;
          //  mRadarScan_.addDataList(data, f, Main6Activity.this, cb);
//            for (int i = 0; i < 100; i++) {
//                data.add("23456789"+i);
//            }
//            mRadarScan_1.addDataList(data,false,this);
//            for (int i = 0; i < 100; i++) {
//                data.add( "3456789"+i);
//            }
//            mRadarScan_1.addDataList(data,false,this);
//            for (int i = 0; i < 100; i++) {
//                data.add("456789"+i);
//            }
//            mRadarScan_1.addDataList(data,false,this);
//            for (int i = 0; i < 14; i++) {
//                data1.put(14 + i + "", R.drawable.adg);
//            }
//
//
//            mRadarScanAdapter = new RadarScan.RadarScanAdapter();
//            mRadarScanAdapter.setDataList(data);
//            mRadarScanAdapter.setData(data1);
////        mRadarScanAdapter.setIcondp(15);
////        mRadarScanAdapter.setInterval(45);
//            mRadarScanAdapter.setIconCenter(R.mipmap.ic_launcher);
//            //  mRadarScanAdapter.s
//            mRadarScan.setAdapter(mRadarScanAdapter, false);
////        Map
//            final Map<RadarScan.DeviceCategory, List<String>> map = new HashMap();
//            map.put(RadarScan.DeviceCategory.DIAPER, data);
//            mRadarScanAdapter.setDataListMap(map);
//            mRadarScan.setAdapter(mRadarScanAdapter, true);
//
//            //  Drawable d=getResources().getDrawable(R.mipmap.ic_launcher);
//            //  Log.i("trag", "onCreate: "+d.getClass().getName());
//
            mh = new Handler();
            r = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (f) {

                            mRadarScan_.addDataList(data, f, Main6Activity.this, cb);
//                     //   mRadarScanAdapter.setDataList(data);
//                        mRadarScanAdapter.setData(data1);
//                        mRadarScan.setAdapter(mRadarScanAdapter, false);
                        } else {
                            mRadarScan_.addDataList(data, f, Main6Activity.this, cb);
//                        mRadarScanAdapter.setDataListMap(map);
//                      //  mRadarScanAdapter.setDataList(data);
////                        mRadarScanAdapter.setData(data1);
//                        mRadarScan.setAdapter(mRadarScanAdapter, true);
                        }
                        // mRadarScan.setAnimation(f);
                        f = f ? false : true;
                        mh.postDelayed(r, 10 * 1000);
                    }catch (Exception e){
                        Log.e(TAG, "run: ",e );
                    }
                }
            };
               mh. postDelayed(r,10*1000);
////            for (int i = 0; i < 20; i++) {
////                Thread d = new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        synchronized (Main6Activity.class) {
////                            try {
////                                Thread.sleep(100);
////                                Log.i("Runnable", "run: " + Thread.currentThread().getId());
////                                Thread.sleep(100);
////                                Log.i("Runnable", "run: " + Thread.currentThread().getId());
////                            } catch (InterruptedException e) {
////                                e.printStackTrace();
////                            }
////                        }
////                    }
////                });
////                Log.i("Runn11able", "run: " + d.getId());
////              //  d.setPriority(i+1);
////                d.start();
////            }
//
//            // ATTENTION: This was auto-generated to implement the App Indexing API.
//            // See https://g.co/AppIndexing/AndroidStudio for more information.
//            //   client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        } catch (Exception e) {
            Log.e("Exception", "onCreate: ", e);
        }
    }

    public int dip2px(float dip) {
        float density = getResources().getDisplayMetrics().density;// 设备密度
//        Log.i(TAG, "dip2px: "+density);
        int px = (int) (dip * density + 0.5f);// 3.1->3, 3.9+0.5->4.4->4
        return px;
    }

    public void next(View view) {
        Intent intent=new Intent(Main6Activity.this,Main7Activity.class);
        startActivity(intent);
    }

    public void last(View view) {
        Intent intent = new Intent(Main6Activity.this, Main4Activity.class);
        startActivity(intent);
    }
int count=8;
    public void add(View view) {
        try {
//            List<String> data1 = new ArrayList<>();
//            mRadarScanAdapter.setDataList(data1);
//            for (int i = 0; i < 1; i++) {
//                data1.add(count + i + "");
//                count++;
//            }
            //  mRadarScan.setAdapter(mRadarScanAdapter,false);
            mRadarScan_.exitRadarScan();
            Intent i = new Intent(this, NotificationService.class);
            i.putExtra(NotificationService.NSinfo.TITIL, "title");
            i.putExtra(NotificationService.NSinfo.CONTENT, "CONTENT");
            i.putExtra(NotificationService.NSinfo.TICKER, "TICKER");
            startService(i);
        }catch (Exception e){
            Log.e(TAG, "add: ",e );
        }

    }




}
