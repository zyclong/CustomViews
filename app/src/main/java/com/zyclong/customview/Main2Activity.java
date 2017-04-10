package com.zyclong.customview;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.abellstarlite.wedgit.zyclong.ProgerssBarARC;
import com.abellstarlite.wedgit.zyclong.SeekBarWithNumber;

public class Main2Activity extends AppCompatActivity {

    ProgerssBarARC progerssBarARC;
    SeekBarWithNumber seekBarWithNumber;
    Handler my;
    private String TAG = "Main2Activity";

    AnimationDrawable ani;
    RotateAnimation rAnima;
    AnimationSet set = new AnimationSet(false);
    boolean isAnim=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
         //   progerssBarARC = (ProgerssBarARC) findViewById(R.id.ProgerssBarARC);
         //   progerssBarARC.setmProgress(40);


           final ImageView img_loading_circle= (ImageView) findViewById(R.id.ImageView);
            final Animation circle_anim = AnimationUtils.loadAnimation(this, R.anim.rotateanim);
            LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
            circle_anim.setInterpolator(interpolator);
            if (circle_anim != null) {
                isAnim=true;
                img_loading_circle.startAnimation(circle_anim);  //开始动画
            }
//            progerssBarARC.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(isAnim){
//                        img_loading_circle.clearAnimation();
//                        isAnim=false;
//                    }else {
//                        isAnim=true;
//                        img_loading_circle.startAnimation(circle_anim);  //开始动画
//                    }
//                }
//            });
            //  rAnima = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.anim_progressbar);
            //   rAnima.setDuration(2000);
            //    progerssBarARC.setAnimation(rAnima);


//            /**
//             *  动画 1. 旋转动画
//             *  fromDegrees:开始度数
//             *  pivotXType：中心点x方向的值类型
//             *  pivotXValue：中心点x方向的值
//             *
//             */
//            RotateAnimation rotateAnimation=new RotateAnimation(
//                    0, 360,
//                    Animation.RELATIVE_TO_SELF, 0.5f,
//                    Animation.RELATIVE_TO_SELF, 0.5f);
//            rotateAnimation.setDuration(3000);//设置时长
//            rotateAnimation.setFillAfter(true);//设置最终状态为填充效果
//
//            //添加旋转动画到动画集合中
//            set .addAnimation(rotateAnimation);
//
//            /**动画2： 缩放动画
//             *
//             */
///*		ScaleAnimation scaleAnimation=new ScaleAnimation(
//				0, 1,
//				0, 1,
//				welcomeImageView.getWidth()/2,
//				welcomeImageView.getHeight()/2);*/
//            ScaleAnimation scaleAnimation=new ScaleAnimation(
//                    0, 1,
//                    0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            scaleAnimation.setDuration(3000);//设置时长
//            scaleAnimation.setFillAfter(true);//设置最终状态为填充效果
//            //添加旋转动画到动画集合中
//            set .addAnimation(scaleAnimation);
//            progerssBarARC.startAnimation(set);

            //   progerssBarARC.setBackgroundResource(R.drawable.bg);

            //   ani = (AnimationDrawable) progerssBarARC.getBackground();


//            /** 设置旋转动画 */
//            final RotateAnimation animation =new RotateAnimation(0f,360f, Animation.RELATIVE_TO_SELF,
//                    0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//            animation.setDuration(3000);//设置动画持续时间
///** 常用方法 */
////animation.setRepeatCount(int repeatCount);//设置重复次数
////animation.setFillAfter(boolean);//动画执行完后是否停留在执行完的状态
////animation.setStartOffset(long startOffset);//执行前的等待时间
//            start.setOnClickListener(new OnClickListener() {
//                public void onClick(View arg0) {
//                    image.setAnimation(animation);
///** 开始动画 */
//                    animation.startNow();
//                }
//            });
//            cancel.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
///** 结束动画 */
//                    animation.cancel();
//                }
//            });

            my = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                 //   progerssBarARC.setmProgress(msg.what);
                    //  seekBarWithNumber.setProgress(msg.what);
                    sendEmptyMessageDelayed((msg.what + 5) % 100, 1000);
                    //   progerssBarARC.startAnimation(rAnima);
                    if (msg.what == 0) {
                        //  progerssBarARC.setAnimation(false);
                    }
                    if (msg.what == 50) {
                        //  progerssBarARC.setAnimation(true);
                    }
                    if (ani != null) {
                        if (!ani.isRunning()) {

                            //        ani.start();
                        }
                    } else {
                        Log.i(TAG, "handleMessage: ani is null");
                    }
                    //  Log.i(TAG, "handleMessage:msg.what+5)%100 "+(msg.what+5)%100);
                }
            };
//        my.sendEmptyMessageDelayed(0,1000);

            seekBarWithNumber = (SeekBarWithNumber) findViewById(R.id.SeekBarWithNumber);
            seekBarWithNumber.setMinInvertal(10);
//            seekBarWithNumber.setDrawableIDs(R.drawable.diaper_btn_remind_setting, R.drawable.diaper_bar_remind_setting,100,false);
            SeekBarWithNumber s1 = (SeekBarWithNumber) findViewById(R.id.SeekBarWithNumber1);
            s1.setDrawableIDs(R.drawable.location_setting_alarm_sel, R.drawable.location_setting_line_alarm, 3, false);
            s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Log.i(TAG, "onProgressChanged: " + seekBar.getProgress());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    Log.i(TAG, "onProgressChanged:s " + seekBar.getProgress());

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Log.i(TAG, "onProgressChanged:e " + seekBar.getProgress());

                }
            });

        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
        }
    }

    public void next(View view) {
        Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
        startActivity(intent);
    }

    public void last(View view) {
        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
        startActivity(intent);
    }


}
