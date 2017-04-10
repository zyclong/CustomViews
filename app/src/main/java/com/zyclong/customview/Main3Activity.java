package com.zyclong.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.BaseAdapter;

import com.abellstarlite.bean.Interface.IProbleEventBean;
import com.abellstarlite.wedgit.zyclong.BarChart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Main3Activity extends AppCompatActivity {

    private String TAG="Main3Activity";
    private String startTime= DateFormat.format("yy-MM-dd ",
            new Date(System.currentTimeMillis())).toString();
    private int number=140;
    String sssss="2017-03-01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        ArrayList<IProbleEventBean> al=new ArrayList();

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
        Random r=new Random();
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
            long t=(long)(Math.random()*dayTime)*7;
            // r.
          //  Log.i(TAG, "initViews: max"+t);

            int  day=i%5;
            time=dayTime*day+smdate.getTime();

            time+=t;
            final String tt=sdf.format(new Date(time));
            //Log.i(TAG, "initViews: "+tt);
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
        BarChart mB= (BarChart) findViewById(R.id.BarChart);
//        mB.setDataSource(al,"2017-03-01");
        mB.setDataSource(al,sssss);


    }

    public void next(View view){
        Intent intent=new Intent(Main3Activity.this,Main4Activity.class);
        startActivity(intent);
    }

    public void last(View view){
        Intent intent=new Intent(Main3Activity.this,Main2Activity.class);
        startActivity(intent);
    }
}
