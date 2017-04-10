package com.zyclong.customview;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import com.abellstarlite.bean.Interface.IProbleEventBean;
import com.abellstarlite.wedgit.zyclong.DataDayTextView;
import com.abellstarlite.wedgit.zyclong.DataDayView;
import com.abellstarlite.wedgit.zyclong.ProgerssBarARC;
import com.orhanobut.logger.Logger;
import com.zyclong.customviewlibrary.CircleView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    public static final String TAG ="MainActivity" ;
    private CircleView mCircleBar;
    private ProgerssBarARC mProgerssBarARC;
    private DataDayView mDataDayView;
    private String startTime= DateFormat.format("yy-MM-dd ",
            new Date(System.currentTimeMillis())).toString();
    private int number=1000;
    String sssss="2017-03-28";
    private Handler mh;
    private boolean f;
    private Runnable r1;
    ArrayList<IProbleEventBean> al=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // Log
        super.onCreate(savedInstanceState);

        setContentView(R.layout.circle_view);

        try{
        initViews();
       }catch (Exception e){
            Log.e(TAG, "onCreate: ",e );
      }


    }

//    private void initViews() {
//        Button b= (Button) findViewById(R.id.share);
//       final String filep=Environment.getExternalStorageDirectory().getAbsolutePath()+"/1.jpeg";
//        b.setOnClickListener(new View.OnClickListener() {
//
//            private String filepath=filep;
//            @Override
//            public void onClick(View v) {
//                Bitmap b=takeScreenShot(MainActivity.this);
//                if(b==null){
//                    Log.i(TAG, "onClick: no bitmap" );
//                }
//                savePic(b,filep);
//               // shareToFriend(b);
//               File f= new File (filepath);
//                if(f.exists()){
//                   shareToFriend(new File (filepath));
//                   // shareToFriend(new File (filepath));
//
//                }else {
//                    Log.i(TAG, "onClick: no file" +f.getAbsolutePath());
//                }
//            }
//        });
//    }


    private void initViews() {


      //  mCircleBar = (CircleView) findViewById(R.id.circleProgressbar);
        mDataDayView = (DataDayView) findViewById(R.id.dataDayView);



        String s=sssss+" 01:23:51";
        Long dayTime=24*60*60*1000l;
        final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date smdate=null;
        try {
          //  new Date(time);
            smdate = sdf.parse(sssss+" 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Random r=new Random();
         Long time=1488619431000l;
      //  Calendar c=Calendar.getInstance();
      //  c.setTime(new Date(time));
     //   Log.i(TAG, "1488561831000l:"+(c.getTimeInMillis()));
     //   Log.i(TAG, "1488561831000l:"+(c.getTime()));
      //  Log.i(TAG, "1488561831000l:"+(sdf.format(new Date(time))));
     //   try{
     //   Log.i(TAG, "1488561831000l:"+(sdf.parse(s).getTime()));
     //   }catch (Exception e){
    //        Log.e(TAG, "initViews: ", e);
      //  }


        int max=60;
        int leanth=number;
        for(int i = 0; i<leanth; i++){
            //int i1= (int) (Math.random()*10);
            long t=(long)(Math.random()*dayTime)*5;
           // r.
          //  Log.i(TAG, "initViews: max"+t);

             int  day=i%5;
                time=dayTime*day+smdate.getTime();

            time+=t;
            final String tt=sdf.format(new Date(time));
          //  Log.i(TAG, "initViews: "+tt);
            IProbleEventBean ip=new IProbleEventBean() {
                String s=tt;
                @Override
                public String getKind() {
                    int i= (int) (Math.random()*10);
                   // Log.i(TAG, "getKind: "+i);
                    return i%2==0 ? "P" : "C";
                }

                @Override
                public String getEvent_time() {
                   // Calendar c=Calendar.getInstance();
                  //  c.setTimeInMillis(time);

                    return s;
                }
            };
            al.add(ip );

        }
    //    Log.i(TAG, "initViews: "+al.size());
      //  al=null;
        mDataDayView.setDataDay(al,sssss, (DataDayTextView) findViewById(R.id.dataDayTextView));
        mh = new Handler();
        r1 = new Runnable() {
            @Override
            public void run() {
                try {
                    if (f) {
                        mDataDayView.setDataDay(al,sssss, (DataDayTextView) findViewById(R.id.dataDayTextView));

                    } else {

                        ArrayList<IProbleEventBean> d=new ArrayList();
                        mDataDayView.setDataDay(d,sssss, (DataDayTextView) findViewById(R.id.dataDayTextView));
                    }
                    // mRadarScan.setAnimation(f);
                    f = f ? false : true;
                    mh.postDelayed(r1,   10);
                    Log.d(TAG, "run: post");
                    Log.i(TAG, "run: post");
                }catch (Exception e){
                    Log.e(TAG, "run: ",e );
                }
            }
        };
        mh. postDelayed(r1,10);
        Log.i(TAG, "run: post");

     //   mCircleBar.setProgress(80);
//        mProgerssBarARC= (ProgerssBarARC) findViewById(R.id.ProgerssBarARC);
//        mProgerssBarARC.setmProgress(100);
        Logger.init(TAG)                 // default PRETTYLOGGER or use just init()
                .methodCount(30)                 // default 2
                .hideThreadInfo()               // default shown
        // .logLevel(LogLevel.NONE)        // default LogLevel.FULL
        // .methodOffset(0)                // default 0
        //   .logAdapter(new AndroidLogAdapter())
        ; //default AndroidLogAdapter

       // Logger.d("hello");
       // Logger.e("hello");
       // Logger.w("hello");
       // Logger.v("hello");
        //Logger.wtf("hello");
        //  Logger.json(JSON_CONTENT);
        //   Logger.xml(XML_CONTENT);
        //  Logger.log(DEBUG, "tag", "message", throwable);
    }

    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }


    /**
     * 分享信息到朋友
     *
     * @param file,假如图片的路径为path，那么file = new File(path);
     */
    private void shareToFriend(File file) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(componentName);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_TEXT, "测试微信");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(intent);
    }

    // 保存到sdcard
    public static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享信息到朋友圈
     *
     * @param file，假如图片的路径为path，那么file = new File(path);
     */
    private void shareToTimeLine(File file) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(componentName);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//   intent.setAction(android.content.Intent.ACTION_SEND_MULTIPLE);
//   ArrayList<Uri> uris = new ArrayList<Uri>();
//   for (int i = 0; i < images.size(); i++) {
//     Uri data = Uri.fromFile(new File(thumbPaths.get(i)));
//     uris.add(data);
//   }
//   intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.setType("image/*");
        startActivity(intent);
    }


    public void next(View view){
        Intent intent=new Intent(MainActivity.this,Main2Activity.class);
        startActivity(intent);
    }
}
